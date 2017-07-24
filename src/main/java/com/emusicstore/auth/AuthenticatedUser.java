package com.emusicstore.auth;

import com.emusicstore.model.Role;
import com.emusicstore.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class AuthenticatedUser extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = 1L;
    static final Logger logger = LoggerFactory.getLogger(AuthenticatedUser.class);
    private User user;

    public AuthenticatedUser(User user) {
        super(user.getUsername(), user.getPassword(), getAuthorities(user));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Set<Role> roles = user.getRoles();

        for (Role role : roles) {
            logger.info("Role : {}", role);
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        logger.info("authorities : {}", authorities);
        return authorities;
    }

}
