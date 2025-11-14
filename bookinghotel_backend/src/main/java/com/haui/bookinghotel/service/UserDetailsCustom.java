package com.haui.bookinghotel.service;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component("userDetailsService")
public class UserDetailsCustom implements UserDetailsService {

    private final UserService userService;
    public UserDetailsCustom(UserService userService) {
        this.userService = userService;
    }
    // bắt lỗi khi người dùng nhập sai username hoặc pass
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.haui.bookinghotel.domain.User user = this.userService.handleFindUserByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("Username/password k hop le");
        }
        if (user.getRole() == null) {
            throw new UsernameNotFoundException("User khong co quyen");
        }
        String roleName = user.getRole().name();

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority( roleName.toUpperCase()) // Thêm "ROLE_" và "UPPERCASE"
        );
        return new User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
