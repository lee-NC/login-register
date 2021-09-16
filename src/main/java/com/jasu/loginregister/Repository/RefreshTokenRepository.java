package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.RefreshToken;
import com.jasu.loginregister.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;
import java.util.Set;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(User user);

    Set<RefreshToken> findAllByCreatedBy(String userId);

    Set<RefreshToken> findAllByCreatedByAndDeleted(Long userId, boolean deleted);
}
