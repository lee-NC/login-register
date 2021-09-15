package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.TutorStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface TutorStudentRepository extends JpaRepository<TutorStudent,Long> {
}
