package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.School;
import com.jasu.loginregister.Entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SchoolRepository extends JpaRepository<School,Long> {
    boolean existsByTutorAndSchoolName(Tutor tutor, String schoolName);
}
