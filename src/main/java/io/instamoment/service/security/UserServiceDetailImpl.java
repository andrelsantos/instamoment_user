package io.instamoment.service.security;

import io.instamoment.service.commands.SecurityCommand.outputs.UserDetailsDTO;
import io.instamoment.service.entity.User;
import io.instamoment.service.exception.UserFoundException;
import io.instamoment.service.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserServiceDetailImpl implements UserDetailsService {

    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = service.loadUserLogin(username);
        if (!user.isPresent()) {
            throw new UserFoundException("");
        } 

        user.get().setPassword(new BCryptPasswordEncoder().encode(user.get().getPassword()));
        return new UserDetailsDTO(user);
    }
}
