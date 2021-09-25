package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.RefreshToken;
import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Exception.ForbiddenException;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Repository.RefreshTokenRepository;
import com.jasu.loginregister.Repository.UserRepository;
import com.jasu.loginregister.Service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.REFRESH_EXP_DATE;


@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {


  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private UserRepository userRepository;

  public RefreshToken findByToken(String token) {
    RefreshToken refreshToken = refreshTokenRepository.findByToken(token);
    if (refreshToken==null){
      throw new NotFoundException("Token unavailable");
    }
    return refreshToken;
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

  public boolean verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      token.setDeleted(true);
      refreshTokenRepository.saveAndFlush(token);
      User user = userRepository.getById(Long.parseLong(token.getCreatedBy()));
      user.setState("LOGOUT");
      userRepository.saveAndFlush(user);
      return false;
    }
    return true;
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
      User user = userRepository.getById(Long.parseLong(refreshToken.getCreatedBy()));
      user.setState("LOGOUT");
      userRepository.saveAndFlush(user);
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

  @Override
  public void updateByDelete() {
    log.info("Check refresh token up to date ");
    try {
      Date date = new Date();
      List<RefreshToken> tokens = refreshTokenRepository.findAllByDeleted(false);
      if (tokens.isEmpty()){
        return;
      }
      for (RefreshToken token: tokens){
        if (token.getExpiryDate().isBefore(date.toInstant())){
          token.setDeleted(true);
          refreshTokenRepository.saveAndFlush(token);
          User user = userRepository.getById(Long.parseLong(token.getCreatedBy()));
          user.setState("LOGOUT");
          userRepository.saveAndFlush(user);
        }
      }
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
  }
}
