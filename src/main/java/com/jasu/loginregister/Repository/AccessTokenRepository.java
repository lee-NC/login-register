package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.AccessToken;
import com.jasu.loginregister.Entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface AccessTokenRepository extends JpaRepository<AccessToken,Long> {


    AccessToken findByAccessToken(String token);

    AccessToken findByRefreshToken(RefreshToken refreshToken);
}
