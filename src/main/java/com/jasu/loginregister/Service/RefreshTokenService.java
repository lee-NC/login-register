package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken createRefreshToken(Long userId);
    RefreshToken verifyExpiration(RefreshToken token);
    void deleteByUserId(Long userId);
    boolean checkTimeLogin(String toString);
}
