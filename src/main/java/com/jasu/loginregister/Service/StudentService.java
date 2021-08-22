package com.jasu.loginregister.Service;

import com.jasu.loginregister.Model.Dto.StudentDto;
import com.jasu.loginregister.Model.Dto.UserDto;
import com.jasu.loginregister.Model.Request.CreateStudentRequest;

public interface StudentService {
    StudentDto createStudent(CreateStudentRequest createStudentRequest);

}
