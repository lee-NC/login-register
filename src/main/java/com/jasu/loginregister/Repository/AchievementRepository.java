package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.Achievement;
import com.jasu.loginregister.Entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface AchievementRepository extends JpaRepository<Achievement,Long> {
//    @Query(value = "SELECT jasu_tutor_achievement.id FROM jasu_user " +
//            "INNER JOIN jasu_tutor_achievement ON jasu_user.id = jasu_tutor_achievement.tutor_id " +
//            "WHERE jasu_user.id =:userId " +
//            "AND jasu_tutor_achievement.achievement = :achievement " +
//            "AND jasu_tutor_achievement.year = :year ",
//            nativeQuery = true)
//    List<Long> checkExist(@Param("userId") Long userId,
//                          @Param("achievement") String achievement, @Param("year") int year);

    boolean existsByTutorAndAchievementAndYear(Tutor tutor, String achievement, int year);
}
