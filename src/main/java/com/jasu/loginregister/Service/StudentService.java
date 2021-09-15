package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.Student;
import com.jasu.loginregister.Model.Request.CreatedToUser.CreateStudentRequest;

import java.util.List;

public interface StudentService {
    Student createStudent(CreateStudentRequest createStudentRequest);

    Student findByUserId(Long userId);

    List<Student> getByListUserId(List<Long> userIds);

    Student updateStudent(Student student);
}
