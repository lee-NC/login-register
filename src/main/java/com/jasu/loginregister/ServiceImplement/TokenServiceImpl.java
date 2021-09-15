package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.ClassStudent;
import com.jasu.loginregister.Entity.Token;
import com.jasu.loginregister.Exception.ForbiddenException;
import com.jasu.loginregister.Repository.TokenRepository;
import com.jasu.loginregister.Service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    public Token createToken(Token token){
        log.info("Create token");
        token.setDeleted(false);
        return tokenRepository.saveAndFlush(token);
    }

    @Override
    public Token findByToken(String token) {
        log.info("Find token");
        return tokenRepository.findByToken(token);
    }

    @Override
    public void deleteAllOldToken(String userId) {
        log.info("Delete old token");
        Set<Token> tokens = tokenRepository.findAllByCreatedByAndDeleted(userId,false);
        for (Token token: tokens){
            token.setDeleted(true);
            tokenRepository.saveAndFlush(token);
        }
    }

    @Override
    public boolean checkTimeLogin(String userId){
        log.info("Check recent class in Service");

        //get Now day
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String []nowDay = formatter.format(date).split("/");

        int limitRank = 15;
        //get list recent class
        Set<Token> tokens = tokenRepository.findAllByCreatedBy(userId);
        for (Token token: tokens){
            String []day = token.getCreatedAt().split("/");
            if (Integer.parseInt(day[1])==Integer.parseInt((nowDay[0])))   limitRank--;
        }
        if (limitRank <= 0) return false;
        return true;
    }

}
