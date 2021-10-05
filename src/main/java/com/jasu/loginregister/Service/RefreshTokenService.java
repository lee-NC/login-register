package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.RefreshToken;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken findByToken(String token);
    RefreshToken createRefreshToken(Long userId);
    boolean verifyExpiration(RefreshToken token);
    void deleteByUserId(Long userId);
    boolean checkTimeLogin(String toString);

    void updateByDelete();

    void updateRefreshToken(RefreshToken refreshToken);
}
