package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
@Transactional
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    boolean existsByUserIdAndRoleKey(Long userId, String roleKey);
    Set<UserRole> findAllByUserId(Long userId);
}
