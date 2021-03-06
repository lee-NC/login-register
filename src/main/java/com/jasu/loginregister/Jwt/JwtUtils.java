package com.jasu.loginregister.Jwt;

import com.jasu.loginregister.Jwt.Principal.UserPrincipal;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.*;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  public String generateJwtToken(UserPrincipal userPrincipal) {
    return generateTokenFromUserId(userPrincipal.getId());
  }

  public String generateTokenFromUserId(Long userId) {
    return Jwts.builder().setSubject(userId.toString()).setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + TIME_PER_HOUR)).signWith(SignatureAlgorithm.HS512,SECRET)
        .compact();
  }

  public String getUserIdFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(SECRET).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

}
