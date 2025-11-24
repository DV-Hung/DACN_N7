package com.haui.bookinghotel.controller;

import com.haui.bookinghotel.domain.User;
import com.haui.bookinghotel.domain.request.LoginDTO;
import com.haui.bookinghotel.domain.response.auth.ResLoginDTO;
import com.haui.bookinghotel.domain.response.user.ResUserDTO;
import com.haui.bookinghotel.service.UserService;
import com.haui.bookinghotel.util.SecurityUtil;
import com.haui.bookinghotel.util.annotation.ApiMessage;
import com.haui.bookinghotel.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${hungdeptrai.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
                           UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/")
    public ResponseEntity<String> home(){
        return ResponseEntity.ok().body("Hello World");
    }
    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        //Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        //xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // Tao token
        String accessToken = this.securityUtil.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO resLoginDTO = new ResLoginDTO();
        resLoginDTO.setAccess_token(accessToken);

        //create refresh token
        String refreshToken = this.securityUtil.createRefreshToken(loginDTO.getUsername(),resLoginDTO);

        //update user
        this.userService.updateUserToken(refreshToken, loginDTO.getUsername());

        //set cookies
        ResponseCookie resCookie = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body(resLoginDTO);
    }

    @PostMapping("/register")
    @ApiMessage("Register a new user")
    public ResponseEntity<ResUserDTO> register(@Valid @RequestBody User postmanUser ) throws IdInvalidException
    {
        boolean isEmailExist = this.userService.isEmailExist(postmanUser.getEmail());
        if(isEmailExist)
        {
            throw new IdInvalidException("Email "+postmanUser.getEmail()+ " đã tồn tại, vui lòng sử dung email khác !!!");
        }
        String hashPassword = this.passwordEncoder.encode(postmanUser.getPassword());
        postmanUser.setPassword(hashPassword);
        User newUser = userService.handleCreateUser(postmanUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResUserDTO(newUser));
    }

    @PostMapping("/logout")
    @ApiMessage("Logout User")
    public ResponseEntity<Void> logout(
            @CookieValue(name= "refresh_token", defaultValue = "") String refreshToken
    ) throws IdInvalidException {
        String email= SecurityUtil.getCurrentUserLogin().isEmpty() ? "" : SecurityUtil.getCurrentUserLogin().get();
        if(email.equals("")){
            throw new IdInvalidException("Access token không hợp lệ");
        }

        //remove refresh token cookie
        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                .body(null);
    }

    @GetMapping("/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount(){
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get() : "";

        User currentUserDB = this.userService.handleFindUserByUsername(email);
        ResLoginDTO.UserLoginDTO resLoginDTO = new ResLoginDTO.UserLoginDTO();
        ResLoginDTO.UserGetAccount resGetAccount = new ResLoginDTO.UserGetAccount();
        if(currentUserDB != null)
        {
            resLoginDTO.setId(currentUserDB.getId());
            resLoginDTO.setName(currentUserDB.getUsername());
            resLoginDTO.setEmail(currentUserDB.getEmail());
            resLoginDTO.setRole(currentUserDB.getRole());
            resGetAccount.setUser(resLoginDTO);
        }
        return ResponseEntity.ok().body(resGetAccount);
    }

}
