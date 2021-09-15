package com.jasu.loginregister.Entity;

import org.springframework.security.core.GrantedAuthority;

public enum DeRole implements GrantedAuthority {
    ADMIN, STAFF, TUTOR, STUDENT,USER;

    public String getAuthority() {
        return name();
    }

}
