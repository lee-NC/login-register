package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SubjectRepository extends JpaRepository<Subject,Long> {
    Subject findBySubjectName(String subjectName);
}
