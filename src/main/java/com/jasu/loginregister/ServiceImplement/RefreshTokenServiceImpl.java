package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.RefreshToken;
import com.jasu.loginregister.Exception.ForbiddenException;
import com.jasu.loginregister.Repository.RefreshTokenRepository;
import com.jasu.loginregister.Repository.UserRepository;
import com.jasu.loginregister.Service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static com.jasu.loginregister.Entity.DefineEntityStateMessage.REFRESH_EXP_DATE;


@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {


  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private UserRepository userRepository;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(Long userId) {
    RefreshToken refreshToken = new RefreshToken();
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();
    refreshToken.setUser(userRepository.findById(userId).get());
    refreshToken.setExpiryDate(Instant.now().plusMillis(REFRESH_EXP_DATE));
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setDeleted(false);
    refreshToken.setCreatedAt(formatter.format(date));
    refreshToken.setCreatedBy(userId.toString());
    refreshToken = refreshTokenRepository.saveAndFlush(refreshToken);
    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new ForbiddenException("Refresh token was expired. Please make a new login request");
    }

    return token;
  }

  @Transactional
  public int deleteByUserId(Long userId) {
    return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
  }
}
