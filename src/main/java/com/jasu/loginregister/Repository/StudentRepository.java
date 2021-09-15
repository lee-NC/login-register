package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.Student;
import com.jasu.loginregister.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUserStudentId(Long userId);
}
