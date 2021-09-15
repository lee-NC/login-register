package com.jasu.loginregister.Jwt.Principal;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class UserPrincipal implements UserDetails {

    private Long id;

    private String fullName;

    private String avatar;

    private Long coin;

    private int numActive;

    private Collection authorities;


    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
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

    @Override
    public boolean isEnabled() {
        return false;
    }
}
