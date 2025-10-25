package com.haui.bookinghotel.service;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

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
        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
