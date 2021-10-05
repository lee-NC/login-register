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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.*;

@RestController
@Slf4j
@RequestMapping("/registry")
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
    private EmailService emailService;


    @PostMapping("")
    public ResponseEntity<?> registryUser(@Valid @RequestBody CreateUserRequest createUserRequest){
        log.info("Registry User in Controller");
        User saveUser = UserMapper.toUser(createUserRequest);
        UserDto userDto = userService.createUser(saveUser);
        userRoleService.createUserRole(userDto.getId(), DeRole.USER.getAuthority());
        User checkUser = userService.findByEmail(createUserRequest.getEmail());
        UserPrincipal userPrincipal = UserMapper.toUserPrincipal(checkUser);
        String jwt = jwtUtils.generateJwtToken(userPrincipal);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userPrincipal.getId());
//        try {
//            sendVerificationEmail(checkUser, "http://localhost:8080/");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        emailService.sendAnEmail(checkUser.getEmail(),VERIFICATION_CONTENT,VERIFICATION_SUBJECT);

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(),
                checkUser.getFullName(),checkUser.getNumActive(),checkUser.getAvatar(), checkUser.getCoin()));
    }

//    private void sendVerificationEmail(User user, String siteURL)
//            throws MessagingException, UnsupportedEncodingException {
//        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
//        VERIFICATION_CONTENT = VERIFICATION_CONTENT.replace("[[name]]",user.getFullName());
//        VERIFICATION_CONTENT = VERIFICATION_CONTENT.replace("[[URL]]",verifyURL);
//        emailService.sendAnEmail(user.getEmail(),VERIFICATION_CONTENT,VERIFICATION_SUBJECT);
//    }

//    @GetMapping("/verify")
//    public ResponseEntity<?> verifyUser(@Param("code") String code) {
//        if (userService.verifyUser(code)) {
//            return ResponseEntity.ok("Verify successfully");
//        } else {
//            return ResponseEntity.badRequest().body("Verify unsuccessfully");
//        }
//    }


    @PostMapping("/tutor")
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

    @PostMapping("/student")
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
