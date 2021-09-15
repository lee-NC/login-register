package com.jasu.loginregister.Controller;


import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Exception.ErrorResponse;
import com.jasu.loginregister.Jwt.JwtUtil;
import com.jasu.loginregister.Model.Dto.BasicDto.*;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Jwt.Principal.UserPrincipal;
import com.jasu.loginregister.Model.Request.CreatedToUser.*;
import com.jasu.loginregister.Service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserRoleService userRoleService;


    @PostMapping("")
    public ResponseEntity<?> registryUser(@Valid @RequestBody CreateUserRequest createUserRequest){
        log.info("Registry User in Controller");
        UserDto userDto = userService.createUser(createUserRequest);
        userRoleService.createUserRole(userDto.getId(), DeRole.USER.getAuthority());
        User checkUser = userService.loginWithEmailAndPassword(createUserRequest.getEmail(),createUserRequest.getPassword());
        UserPrincipal userPrincipal = UserMapper.toUserPrincipal(checkUser);
        Token token = new Token();
        token.setToken(jwtUtil.generateToken(userPrincipal));
        token.setTokenExpDate(jwtUtil.generateExpirationDate());
        token.setCreatedBy(userPrincipal.getId().toString());
        tokenService.deleteAllOldToken(checkUser.getId().toString());
        tokenService.createToken(token);
        return ResponseEntity.ok(token.getToken());
    }

    @PostMapping("/tutor")
    @Secured("USER")
    public ResponseEntity<?> registryTuTor(@Valid @RequestBody CreateTutorRequest createTutorRequest){
        log.info("Registry Tutor in Controller");
        User checkUser = userService.findByID(createTutorRequest.getUserId());
        if (!userRoleService.existUserRole(createTutorRequest.getUserId(),DeRole.TUTOR.getAuthority())){
            userService.updateUser(checkUser);
            Tutor tutor = tutorService.createTutor(createTutorRequest);
            TutorDto tutorDto = UserMapper.toTutorDto(tutor,checkUser);
            userRoleService.createUserRole(checkUser.getId(), DeRole.TUTOR.getAuthority());
            return ResponseEntity.ok(tutorDto);
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST,DefineEntityStateMessage.ACTION_UNSUCCESSFULLY));
    }

    @PostMapping("/student")
    public ResponseEntity<?> registryStudent(@Valid @RequestBody CreateStudentRequest createStudentRequest){
        log.info("Registry Student in Controller");
        User checkUser = userService.findByID(createStudentRequest.getUserId());
        if (!userRoleService.existUserRole(checkUser.getId(), DeRole.STUDENT.getAuthority())){
            userService.updateUser(checkUser);
            Student student = studentService.createStudent(createStudentRequest);
            StudentDto studentDto = UserMapper.toStudentDto(student,checkUser);
            userRoleService.createUserRole(checkUser.getId(), DeRole.STUDENT.getAuthority());
            return ResponseEntity.ok(studentDto);
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST,DefineEntityStateMessage.ACTION_UNSUCCESSFULLY));
    }
}
