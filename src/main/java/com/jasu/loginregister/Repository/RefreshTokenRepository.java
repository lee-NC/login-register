package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.RefreshToken;
import com.jasu.loginregister.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findAllByCreatedBy(String userId);

    List<RefreshToken> findAllByCreatedByAndDeleted (String userId, boolean deleted);

    RefreshToken findTopByCreatedByAndDeleted (String userId, boolean deleted);
}
