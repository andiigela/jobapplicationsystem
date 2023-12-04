package com.ubt.andi.jobapp.security;

import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.services.UserService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;

public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;
    public CustomUserDetailsService(UserService userService){
        this.userService=userService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userService.findUserByUsername(username);
        if(user == null) throw new UsernameNotFoundException("User does not exist!");
        User authUser = new User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.getName())
                ).collect(Collectors.toList())
        );
        return authUser;
    }

}
