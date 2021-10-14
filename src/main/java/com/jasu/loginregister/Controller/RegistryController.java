package com.jasu.loginregister.Controller;


import com.jasu.loginregister.Email.EmailService;
import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage;
import com.jasu.loginregister.Entity.DefinitionEntity.DeRole;
import com.jasu.loginregister.Exception.ErrorResponse;
import com.jasu.loginregister.Jwt.JwtResponse;
import com.jasu.loginregister.Jwt.JwtUtils;
import com.jasu.loginregister.Model.Dto.BasicDto.*;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Jwt.Principal.UserPrincipal;
import com.jasu.loginregister.Model.Request.CreatedToUser.*;
import com.jasu.loginregister.Service.*;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.*;

@RestController
@Slf4j
@RequestMapping("")
public class RegistryController {

    @Autowired
    private UserService userService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    RoleService roleService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    public EmailService emailService;

    @PostMapping("/registry")
    public ResponseEntity<?> registryUser(@Valid @RequestBody CreateUserRequest createUserRequest){
        log.info("Registry User in Controller");
        User saveUser = UserMapper.toUser(createUserRequest);
        UserDto userDto = userService.createUser(saveUser);
        userRoleService.createUserRole(userDto.getId(), DeRole.USER.getAuthority());
        return ResponseEntity.ok("Check your email to verify that you registry with us.");
    }

    @PostMapping("/registry/tutor")
    @PreAuthorize("hasAnyAuthority('USER') && (authentication.principal.id == #createTutorRequest.userId)")
    @Secured("USER")
    public ResponseEntity<?> registryTuTor(@Valid @RequestBody CreateTutorRequest createTutorRequest){
        log.info("Registry Tutor in Controller");
        User checkUser = userService.findByID(createTutorRequest.getUserId());
        if (!userRoleService.existUserRole(createTutorRequest.getUserId(),DeRole.TUTOR.getAuthority())){
            Tutor tutor = tutorService.createTutor(createTutorRequest);
            TutorDto tutorDto = UserMapper.toTutorDto(tutor,checkUser);
            userRoleService.createUserRole(checkUser.getId(), DeRole.TUTOR.getAuthority());
            if (userRoleService.existUserRole(createTutorRequest.getUserId(),DeRole.STUDENT.getAuthority())){
                emailService.sendAnEmail(checkUser.getEmail(),TUTOR_CONTENT,TUTOR_SUBJECT);
            }
            else {
                emailService.sendAnEmail(checkUser.getEmail(),WELCOME_CONTENT,WELCOME_SUBJECT);
            }
            return ResponseEntity.ok(tutorDto);
        }

        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, DEStateMessage.ACTION_UNSUCCESSFULLY));
    }

    @PostMapping("/registry/student")
    @PreAuthorize("hasAnyAuthority('USER') && (authentication.principal.id == #createStudentRequest.userId)")
    @Secured("USER")
    public ResponseEntity<?> registryStudent(@Valid @RequestBody CreateStudentRequest createStudentRequest){
        log.info("Registry Student in Controller");
        User checkUser = userService.findByID(createStudentRequest.getUserId());
        if (!userRoleService.existUserRole(checkUser.getId(), DeRole.STUDENT.getAuthority())){
            Student student = studentService.createStudent(createStudentRequest);
            StudentDto studentDto = UserMapper.toStudentDto(student,checkUser);
            userRoleService.createUserRole(checkUser.getId(), DeRole.STUDENT.getAuthority());
            if (userRoleService.existUserRole(createStudentRequest.getUserId(),DeRole.TUTOR.getAuthority())){
                emailService.sendAnEmail(checkUser.getEmail(),STUDENT_CONTENT,STUDENT_SUBJECT);
            }
            else {
                emailService.sendAnEmail(checkUser.getEmail(),WELCOME_CONTENT,WELCOME_SUBJECT);
            }
            return ResponseEntity.ok(studentDto);
        }

        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, DEStateMessage.ACTION_UNSUCCESSFULLY));
    }
}
