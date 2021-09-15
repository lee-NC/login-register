package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
@Transactional
public interface TokenRepository  extends JpaRepository<Token, Long> {
    Token findByToken(String token);
    Set<Token> findAllByCreatedBy(String userId);
    Set<Token> findAllByCreatedByAndDeleted(String userId,Boolean delete);
}
