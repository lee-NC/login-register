package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.Classroom;
import com.jasu.loginregister.Entity.Tutor;
import com.jasu.loginregister.Entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorRepository extends JpaRepository<Tutor,Long> {

    Tutor findByUserTutorId(Long userId);
}
