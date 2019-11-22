package com.configs.security;

import com.entities.Users;
import com.repositories.UserRepository;
import com.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AccountService accountService;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users users = accountService.loadUserbyUsername(s);
        if (users == null) throw new UsernameNotFoundException("Username not found");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        users.getRoles().forEach(roles -> authorities.add(new SimpleGrantedAuthority(roles.getRole())));
        UserDetails userDetails = new User(users.getUsername(), users.getPassword(), authorities);
        return userDetails;
    }
}
