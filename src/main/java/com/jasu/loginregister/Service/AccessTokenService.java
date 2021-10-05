package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.AccessToken;

public interface AccessTokenService {
    void createAccessToken(AccessToken accessToken);
    AccessToken findByToken(String jwt);
    void updateByDelete();
}
