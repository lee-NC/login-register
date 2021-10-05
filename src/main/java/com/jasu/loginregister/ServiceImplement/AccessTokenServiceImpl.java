package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.AccessToken;
import com.jasu.loginregister.Entity.RefreshToken;
import com.jasu.loginregister.Exception.ForbiddenException;
import com.jasu.loginregister.Repository.AccessTokenRepository;
import com.jasu.loginregister.Service.AccessTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class AccessTokenServiceImpl implements AccessTokenService {

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Override
    public void createAccessToken(AccessToken accessToken) {

        try {
            accessTokenRepository.saveAndFlush(accessToken);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public AccessToken findByToken(String jwt) {
        AccessToken accessToken = accessTokenRepository.findByAccessToken(jwt);
        if (accessToken==null||accessToken.getRefreshToken().getDeleted()){
            throw new ForbiddenException("ACCESS DENIED");
        }
        return accessToken;
    }

    @Override
    @Transactional
    public void updateByDelete() {
        log.info("Update access token in service ");
        try {

            Date date = new Date();
            List<AccessToken> tokens = accessTokenRepository.findAll();
            if (tokens.isEmpty()){
                return;
            }
            for (AccessToken token: tokens){
                if (token.getExpiryDate().isBefore(date.toInstant())){
                    log.info("Update access token in service ");
                    accessTokenRepository.delete(token);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
