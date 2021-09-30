package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.School;
import com.jasu.loginregister.Entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface SchoolRepository extends JpaRepository<School,Long> {
//    @Modifying
//    @Query(value = "SELECT jasu_tutor_school.id FROM jasu_user " +
//            "INNER JOIN jasu_tutor_school ON jasu_user.id = jasu_tutor_school.tutor_id " +
//            "WHERE jasu_user.id =?1 " +
//            "AND jasu_tutor_school.school_name = ?2",
//            nativeQuery = true)
//    List<Long> checkExist(Long userId, String schoolName);

    boolean existsByTutorAndSchoolName(Tutor tutor, String schoolName);
}
