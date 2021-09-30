package com.jasu.loginregister.Jwt.Principal;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class UserPrincipal implements UserDetails {

    private Long id;

    private String username;

    private Collection authorities;

    private boolean enabled;


    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
}
