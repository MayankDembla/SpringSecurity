package com.dembla.spring.security.model;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ROLE_ADMIN, ROLE_CLIENT, ROLE_VENDOR   ;

    @Override
    public String getAuthority() {
        return name();
    }
}
