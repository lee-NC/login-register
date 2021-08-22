package com.jasu.loginregister.Controller;


import com.jasu.loginregister.Entity.Role;
import com.jasu.loginregister.Model.Dto.StudentDto;
import com.jasu.loginregister.Model.Dto.TutorDto;
import com.jasu.loginregister.Model.Dto.UserDto;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Model.Request.CreateStudentRequest;
import com.jasu.loginregister.Model.Request.CreateTutorRequest;
import com.jasu.loginregister.Model.Request.CreateUserRequest;
import com.jasu.loginregister.Service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@Slf4j
@RequestMapping("/registry")
public class RegistryCotroller {

    @Autowired
    private UserService userService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private StudentService studentService;

    @PostMapping("/tutor")
    public ResponseEntity<?> registryTuTor(@Valid @RequestBody CreateTutorRequest createTutorRequest){
        log.info("Registry Tutor in Controller");
        Boolean check = userService.checkUser(createTutorRequest.getCreateUserRequest().getEmail());
        String role = createTutorRequest.getCreateUserRequest().getRoleKey();
        if (check==true&&role.equals("TUTOR")) {
            TutorDto tutorDto = tutorService.createTutor(createTutorRequest);
            return ResponseEntity.ok(tutorDto);
        }
        return ResponseEntity.ok("May be your email in use, please check later ");
    }

    @PostMapping("/student")
    public ResponseEntity<?> registryStudent(@Valid @RequestBody CreateStudentRequest createStudentRequest){
        log.info("Registry Student in Controller");
        Boolean check = userService.checkUser(createStudentRequest.getCreateUserRequest().getEmail());
        String role = createStudentRequest.getCreateUserRequest().getRoleKey();
        if (check==true&&role.equals("STUDENT")) {
            StudentDto studentDto = studentService.createStudent(createStudentRequest);
            return ResponseEntity.ok(studentDto);
        }
        return ResponseEntity.ok("May be your email in use, please check later");
    }



}
