package com.todolist.security;

import com.todolist.domain.CustomUserDetail;
import com.todolist.domain.Users;
import com.todolist.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
public class CustomUserDetailService implements UserDetailsService {

    private final UsersService usersService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users users = null;
        try {
            users = usersService.getOriginalUser(username);
        } catch (Exception e) {
            throw new SecurityException(e);
        }

        List<String> roles = List.of("ROLE_USER");
        return new CustomUserDetail(users , roles);
    }
}

