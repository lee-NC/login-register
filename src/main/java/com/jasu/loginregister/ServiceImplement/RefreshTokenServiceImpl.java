package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.RefreshToken;
import com.jasu.loginregister.Entity.User;
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
import java.util.*;

import static com.jasu.loginregister.Entity.DefineEntityStateMessage.REFRESH_EXP_DATE;


@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {


  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private UserRepository userRepository;

  public Optional<RefreshToken> findByToken(String token) {
    try {
      return refreshTokenRepository.findByToken(token);
    }catch (Exception e){
      throw new ForbiddenException("No token found");
    }
  }

  public RefreshToken createRefreshToken(Long userId) {
    RefreshToken refreshToken = new RefreshToken();
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      Date date = new Date();
      refreshToken.setUser(userRepository.findById(userId).get());
      refreshToken.setExpiryDate(Instant.now().plusMillis(REFRESH_EXP_DATE));
      refreshToken.setToken(UUID.randomUUID().toString());
      refreshToken.setDeleted(false);
      refreshToken.setCreatedAt(formatter.format(date));
      refreshToken.setCreatedBy(userId.toString());
      refreshToken = refreshTokenRepository.saveAndFlush(refreshToken);
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new ForbiddenException("Refresh token was expired. Please make a new login request");
    }
    return token;
  }

  @Override
  public void deleteByUserId(Long userId) {
    log.info("Delete token");
    RefreshToken refreshToken = refreshTokenRepository.findTopByCreatedByAndDeleted(userId.toString(),false);
    if (refreshToken ==null){
      throw new ForbiddenException("ACCESS DENIED");
    }
    try {
      refreshToken.setDeleted(true);
      refreshTokenRepository.saveAndFlush(refreshToken);
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void deleteAllOldToken(Long userId) {
    log.info("Delete all old token");
    try {
      List<RefreshToken> tokens = refreshTokenRepository.findAllByCreatedByAndDeleted(userId.toString(),false);
      for (RefreshToken token: tokens){
        token.setDeleted(true);
        refreshTokenRepository.saveAndFlush(token);
      }
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
  }

  @Override
  public boolean checkTimeLogin(String userId) {
    log.info("Check recent token in Service");

    //get Now day
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date();
    String []nowDay = formatter.format(date).split("/");

    int limitRank = 15;
    try {
      //get list recent token
      List<RefreshToken> tokens = refreshTokenRepository.findAllByCreatedBy(userId);
      if (tokens.isEmpty()) {
        return true;
      }
      log.info("Check recent token in Service");
      for (RefreshToken token: tokens){
        String []day = token.getCreatedAt().split("/");
        if (Integer.parseInt(day[1])==Integer.parseInt((nowDay[0])))   limitRank--;
      }
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
    if (limitRank <= 0) return false;
    return true;
  }
}
