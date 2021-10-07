package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);
    boolean existsByEmail(String email);

    User findByOneTimePassword(String code);

    @Query(value = "SELECT u FROM User u WHERE u.oneTimePassword IS NOT NULL")
    List<User> findUserByExpiredOTP();
}
