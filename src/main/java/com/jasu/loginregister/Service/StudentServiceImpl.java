package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.Student;
import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Exception.DuplicateRecordException;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Model.Dto.StudentDto;
import com.jasu.loginregister.Model.Dto.UserDto;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Model.Request.CreateStudentRequest;
import com.jasu.loginregister.Repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public StudentDto createStudent(CreateStudentRequest createStudentRequest) {
        log.info("Registry Student in Service");

        // Check userId exist
        Student student = UserMapper.toStudent(createStudentRequest);

        studentRepository.saveAndFlush(student);

        return UserMapper.toStudentDto(student);
    }


}
