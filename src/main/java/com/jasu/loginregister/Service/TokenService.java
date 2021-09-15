package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.Token;

public interface TokenService {

    public Token createToken(Token token);

    public Token findByToken(String token);

    void deleteAllOldToken(String userId);

    boolean checkTimeLogin(String userId);
}
