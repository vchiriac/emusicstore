package com.emusicstore.service.impl;

import com.emusicstore.auth.AuthenticatedUser;
import com.emusicstore.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);


    @Autowired
    private UserService userService;


    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String name)
            throws UsernameNotFoundException {
        User user = userService.findByName(name);
        logger.info("User : {}", user);
        if (user == null) {
            logger.info("User not found");
            throw new UsernameNotFoundException("Username not found");
        }
        return new AuthenticatedUser(user);

    }

}