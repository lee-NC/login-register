package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
}
