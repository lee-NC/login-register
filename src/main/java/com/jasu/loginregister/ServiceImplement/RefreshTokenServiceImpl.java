package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.AccessToken;
import com.jasu.loginregister.Entity.RefreshToken;
import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Exception.ForbiddenException;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Repository.AccessTokenRepository;
import com.jasu.loginregister.Repository.RefreshTokenRepository;
import com.jasu.loginregister.Repository.UserRepository;
import com.jasu.loginregister.Service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.*;


@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {


  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private AccessTokenRepository accessTokenRepository;

  @Autowired
  private UserRepository userRepository;

  private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

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

      Date date = new Date();
      refreshToken.setUser(userRepository.findById(userId).get());
      refreshToken.setExpiryDate(Instant.now().plusMillis(REFRESH_EXP_DATE));
      refreshToken.setToken(UUID.randomUUID().toString());
      refreshToken.setDeleted(false);
      refreshToken.setCreatedAt(formatter.format(date));
      refreshToken.setCreatedBy(userId.toString());
      refreshToken.setNumUses(0);
      refreshToken.setDelayTime(new Date(date.getTime()+DELAY_TIME_REFRESH));
      refreshToken = refreshTokenRepository.saveAndFlush(refreshToken);
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
    return refreshToken;
  }

  public boolean verifyExpiration(RefreshToken token) {
    User user = userRepository.getById(Long.parseLong(token.getCreatedBy()));
    if (token.getExpiryDate().compareTo(Instant.now()) < 0||user.getState().equals("LOGOUT")) {
      token.setDeleted(true);
      token.setUpdatedAt(new Date());
      token.setUpdatedAt(new Date());
      refreshTokenRepository.saveAndFlush(token);
      user.setState("LOGOUT");
      userRepository.saveAndFlush(user);
      return false;
    }
    return true;
  }

  @Override
  @Modifying
  public void deleteByUserId(Long userId) {
    log.info("Delete token");
    RefreshToken refreshToken = refreshTokenRepository.findTopByCreatedByAndDeleted(userId.toString(),false);
    AccessToken accessToken = accessTokenRepository.findByRefreshToken(refreshToken);
    if (refreshToken ==null||accessToken==null){
      throw new ForbiddenException("ACCESS DENIED");
    }
    try {
      refreshToken.setDeleted(true);
      refreshToken.setUpdatedAt(new Date());
      refreshTokenRepository.saveAndFlush(refreshToken);

      User user = userRepository.getById(userId);
      user.setState("LOGOUT");
      userRepository.saveAndFlush(user);

      accessTokenRepository.delete(accessToken);

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
  @Modifying
  @Transactional
  public void updateByDelete() {
    log.info("Update refresh token in service ");
    try {
      Date date = new Date();
      List<RefreshToken> tokens = refreshTokenRepository.findAllByDeleted(false);
      if (tokens.isEmpty()){
        return;
      }
      for (RefreshToken token: tokens){
        if (token.getExpiryDate().isBefore(date.toInstant())){
            System.out.println(token.getExpiryDate().toString());
          token.setDeleted(true);
          token.setUpdatedAt(new Date());
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

  @Override
  @Transactional
  @Modifying
  public void updateRefreshToken(RefreshToken refreshToken) {
    try {
      refreshToken.setUpdatedAt(new Date());
      refreshToken.setDelayTime(new Date(new Date().getTime()+DELAY_TIME_REFRESH));
      refreshTokenRepository.saveAndFlush(refreshToken);
      AccessToken accessToken = accessTokenRepository.findByRefreshToken(refreshToken);
      if (accessToken == null){
        return;
      }
      accessTokenRepository.delete(accessToken);

    }catch (Exception e){
      System.out.println(e.getMessage());
    }
  }

  @Override
  public boolean checkNearLoginTime(String userId) {
    RefreshToken refreshToken = refreshTokenRepository.findTopByCreatedBy(userId);
    if (refreshToken.getUpdatedAt().before(new Date(new Date().getTime()-DELAY_TIME_REFRESH))){
      return false;
    }
    return true;
  }

  @Override
  @Transactional
  public void updateExpirationLongTimeToken() {
    List<RefreshToken> refreshTokenList = refreshTokenRepository.findAllByDeleted(true);
    Instant now = (new Date(new Date().getTime() - DELETE_REFRESH_EXP)).toInstant();
    for (RefreshToken refreshToken: refreshTokenList){
      if (refreshToken.getExpiryDate().isBefore(now)){
        refreshTokenRepository.delete(refreshToken);
      }
    }
  }
}
