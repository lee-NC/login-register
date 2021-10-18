package com.jasu.loginregister.Controller;

import com.jasu.loginregister.Email.EmailService;
import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Entity.DefinitionEntity.DeRole;
import com.jasu.loginregister.Exception.ForbiddenException;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Model.Mapper.UserDetailMapper;
import com.jasu.loginregister.Model.Request.CreatedToUser.AchievementRequest;
import com.jasu.loginregister.Model.Request.CreatedToUser.SchoolRequest;
import com.jasu.loginregister.Model.Request.PaymentRequest;
import com.jasu.loginregister.Model.Request.UpdateToUser.*;
import com.jasu.loginregister.Service.*;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.*;
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/upload";

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    public EmailService emailService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER') && (authentication.principal.id == #userId)")
    @Secured({"USER"})
    public ResponseEntity<?> getUserById(@PathVariable("id") Long userId,Model model) {
        log.info("get student by Id in Controller");
        User user = userService.findByID(userId);
        model.addAttribute("userDetailDto",UserDetailMapper.toUserDetailDto(user));
        Student student = null;
        Tutor tutor = null;
        if (userRoleService.existUserRole(userId, DeRole.STUDENT.getAuthority())) {
            student = studentService.findByUserId(userId);
            model.addAttribute("studentDetailDto",UserDetailMapper.toStudentDetailDto(student));
        }
        if (userRoleService.existUserRole(userId, DeRole.TUTOR.getAuthority())) {
            tutor = tutorService.findByUserId(userId);
            model.addAttribute("tutorDetailDto",UserDetailMapper.toTutorDetailDto(tutor));
        }

        return ResponseEntity.ok(model);
    }

    @GetMapping("/coin/{id}")
    @PreAuthorize("hasAnyAuthority('USER') && (authentication.principal.id == #userId)")
    @Secured({"USER"})
    public ResponseEntity<?> getUserCoinById(@PathVariable("id") Long userId, Model model) {
        log.info("get student by Id in Controller");
        User user = userService.findByID(userId);
        model.addAttribute("userId: ",userId);
        model.addAttribute("numActive: ",user.getNumActive());
        model.addAttribute("coin: ",user.getCoin());
        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}/avatar/{filename}")
    @PreAuthorize("hasAnyAuthority('USER') && (authentication.principal.id == #userId)")
    @Secured({"USER"})
    public ResponseEntity<?> download(@Param("filename") String filename, @PathVariable("id") Long userId) {
        File file = new File(UPLOAD_DIR + "/" + filename);
        if (!file.exists()) {
            throw new NotFoundException("File not found");
        }

        UrlResource resource;
        try {
            resource = new UrlResource(file.toURI());
        } catch (MalformedURLException e) {
            throw new NotFoundException("File not found");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @Secured({"ADMIN","STAFF"})
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER') && (authentication.principal.id == #userId)")
    @Secured({"USER"})
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest req, @PathVariable("id") Long userId) {
        User user = userService.updateDetailUser(req, userId);
        return ResponseEntity.ok(UserDetailMapper.toUserDetailDto(user));
    }

    @PutMapping("/student/{id}")
    @PreAuthorize("hasAnyAuthority('STUDENT') && (authentication.principal.id == #userId)")
    @Secured({"STUDENT"})
    public ResponseEntity<?> updateUserStudent(@Valid @RequestBody UpdateStudentRequest req, @PathVariable("id") Long userId,Model model) {
        Student updateStudent = null;
        if (req!=null){
            Student student = studentService.findByUserId(userId);
            if (req.getGrade()>0&&req.getGrade()<13
                    &&req.getGrade()!=student.getGrade()){
                student.setGrade(req.getGrade());
            }
            updateStudent = studentService.updateStudent(student);
        }
        model.addAttribute("studentDetailDto",UserDetailMapper.toStudentDetailDto(updateStudent));
        model.addAttribute("userId",userId);
        return ResponseEntity.ok(model);
    }

    @PutMapping("/tutor/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR') && (authentication.principal.id == #userId)")
    @Secured({"TUTOR"})
    public ResponseEntity<?> updateUserTutor(@Valid @RequestBody UpdateTutorRequest req, @PathVariable("id") Long userId,Model model) {

        Tutor updateTutor = null;
        if (req!=null){
            Tutor tutor = tutorService.findByUserId(userId);
            updateTutor = tutorService.updateTutor(updateTutor(tutor,req));
        }
        model.addAttribute("tutorDetailDto",UserDetailMapper.toTutorDetailDto(updateTutor));
        model.addAttribute("userId",userId);

        return ResponseEntity.ok(model);
    }

    private Tutor updateTutor(Tutor checkTutor, UpdateTutorRequest updateTutorRequest){

        if (updateTutorRequest.getExperience()!=0f&&updateTutorRequest.getExperience()!=checkTutor.getExperience()){
            checkTutor.setExperience(updateTutorRequest.getExperience());
        }
        if (updateTutorRequest.getLiteracy()!=null&&!updateTutorRequest.getLiteracy().equalsIgnoreCase(checkTutor.getLiteracy())){
            checkTutor.setLiteracy(updateTutorRequest.getLiteracy());
        }
        return checkTutor;
    }

    @PutMapping("/achievement/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR') && (authentication.principal.id == #userId)")
    @Secured({"TUTOR"})
    public ResponseEntity<?> updateAchievementTutor(@Valid @RequestBody UpdateAchievementRequest req,
                                                    @PathVariable("id") Long userId, Model model) {
        log.info("update achievement in Controller");
        Tutor tutor = null;
        if (userRoleService.existUserRole(userId, DeRole.TUTOR.getAuthority())) {
            Achievement achievement = achievementService.findById(req.getId());
            if (achievement.getTutor().getUserTutorId()==userId){
                if (req.getAchievement()!=null&&!achievement.getAchievement().equals(req.getAchievement())){
                    achievement.setAchievement(req.getAchievement());
                }
                if (req.getYear()>YEAR_ACHIEVEMENT&&achievement.getYear()!= req.getYear()){
                    achievement.setYear(req.getYear());
                }
                achievementService.checkExist(achievement);
            }
            else {
                return ResponseEntity.badRequest().body("No achievement found");
            }
            tutor = tutorService.findByUserId(userId);
        }
        model.addAttribute("tutorDetailDto",UserDetailMapper.toTutorDetailDto(tutor));
        model.addAttribute("userId",userId);

        return ResponseEntity.ok(model);
    }

    @PostMapping("/achievement/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR') && (authentication.principal.id == #userId)")
    @Secured({"TUTOR"})
    public ResponseEntity<?> createAchievementTutor(@Valid @RequestBody AchievementRequest req,
                                                    @PathVariable("id") Long userId,Model model) {
        log.info("create achievement in Controller");
        Tutor tutor = null;
        if (userRoleService.existUserRole(userId, DeRole.TUTOR.getAuthority())) {
            tutor = tutorService.findByUserId(userId);
            Achievement achievement = new Achievement(tutor,req.getAchievement(),req.getYear());
            if(achievementService.checkExist(achievement)){
                tutor.getAchievements().add(achievement);
                tutorService.updateTutor(tutor);
                tutor = tutorService.findByUserId(userId);
            }
        }
        else {
            return ResponseEntity.badRequest().body("No tutor found");
        }
        model.addAttribute("tutorDetailDto",UserDetailMapper.toTutorDetailDto(tutor));
        model.addAttribute("userId",userId);

        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/achievement/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR') && (authentication.principal.id == #userId)")
    @Secured({"TUTOR"})
    public ResponseEntity<?> deleteAchievementTutor(@Valid @RequestBody DeleteAchievementSchoolRequest req,
                                                    @PathVariable("id") Long userId,Model model) {
        log.info("Delete achievement in Controller");
        Tutor tutor = null;
        if (userRoleService.existUserRole(userId, DeRole.TUTOR.getAuthority())) {
            Achievement achievement = achievementService.findById(req.getId());
            if (achievement.getTutor().getUserTutorId().equals(userId)){
                achievementService.deleteAchievement(achievement);
                tutor = tutorService.findByUserId(userId);
            }
            else {
                return ResponseEntity.badRequest().body("No achievement found");
            }
        }
        else {
            return ResponseEntity.badRequest().body("No tutor found");
        }
        model.addAttribute("tutorDetailDto",UserDetailMapper.toTutorDetailDto(tutor));
        model.addAttribute("userId",userId);

        return ResponseEntity.ok(model);
    }

    @PutMapping("/school/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR') && (authentication.principal.id == #userId)")
    @Secured({"TUTOR"})
    public ResponseEntity<?> updateSchoolTutor(@Valid @RequestBody UpdateSchoolRequest req,
                                               @PathVariable("id") Long userId, Model model) {
        log.info("updateSchool in Controller");
        Tutor tutor = null;
        if (userRoleService.existUserRole(userId, DeRole.TUTOR.getAuthority())) {
            School school = schoolService.findBySchoolID(req.getId());
            if (school.getTutor().getUserTutorId()==userId){
                if (req.getSchoolName()!=null&&!req.getSchoolName().equalsIgnoreCase(school.getSchoolName())){
                    school.setSchoolName(req.getSchoolName());
                }
                schoolService.checkExist(school);
            }
            else {
                return ResponseEntity.badRequest().body("No school found");
            }
            tutor = tutorService.findByUserId(userId);
        }else {
            return ResponseEntity.badRequest().body("No tutor found");
        }
        model.addAttribute("tutorDetailDto",UserDetailMapper.toTutorDetailDto(tutor));
        model.addAttribute("userId",userId);

        return ResponseEntity.ok(model);
    }

    @PostMapping("/school/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR') && (authentication.principal.id == #userId)")
    @Secured({"TUTOR"})
    public ResponseEntity<?> createSchoolTutor(@Valid @RequestBody SchoolRequest req,
                                               @PathVariable("id") Long userId,Model model) {
        log.info("createSchool in Controller");
        Tutor tutor = null;

        if (userRoleService.existUserRole(userId, DeRole.TUTOR.getAuthority())) {
            tutor = tutorService.findByUserId(userId);
            School school = new School(tutor,req.getSchoolName());
            if(schoolService.checkExist(school)){
                tutor.getSchools().add(school);
                tutorService.updateTutor(tutor);
                tutor = tutorService.findByUserId(userId);
            }
        }else {
            return ResponseEntity.badRequest().body("No tutor found");
        }
        model.addAttribute("tutorDetailDto",UserDetailMapper.toTutorDetailDto(tutor));
        model.addAttribute("userId",userId);

        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/school/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR') && (authentication.principal.id == #userId)")
    @Secured({"TUTOR"})
    public ResponseEntity<?> deleteSchoolTutor(@Valid @RequestBody DeleteAchievementSchoolRequest req,
                                               @PathVariable("id") Long userId, Model model) {
        log.info("Delete School in Controller");
        Tutor tutor = null;

        if (userRoleService.existUserRole(userId, DeRole.TUTOR.getAuthority())) {
            School school = schoolService.findBySchoolID(req.getId());
            if (school.getTutor().getUserTutorId()==userId){
                schoolService.deleteSchool(school);
                tutor = tutorService.findByUserId(userId);
            }
            else {
                return ResponseEntity.badRequest().body("No school found");
            }

        }else {
            return ResponseEntity.badRequest().body("No tutor found");
        }
        model.addAttribute("tutorDetailDto",UserDetailMapper.toTutorDetailDto(tutor));
        model.addAttribute("userId",userId);

        return ResponseEntity.ok(model);
    }

    @PostMapping("/recharge/{id}")
    @PreAuthorize("hasAuthority('USER') && (authentication.principal.id == #userId)")
    @Secured("USER")
    public ResponseEntity<?> recharge(@PathVariable("id") Long  userId, @Valid @RequestBody PaymentRequest paymentRequest) {
        log.info("Charge money in Controller");
        User user = userService.findByID(userId);
        if (user.getState().equals("LOGOUT")
                ||!user.getEnabled()
        ){
            throw new ForbiddenException("ACCESS DENIED");
        }
        long coin = 0;
        if (paymentRequest.getFee()>10){
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            switch (paymentRequest.getCurrency()){
                case "VND":{
                    coin = paymentRequest.getFee()/100L;
                    break;
                }
                case "USD":{
                    coin = paymentRequest.getFee()/100L*22753L;
                    break;
                }
                default:{
                    throw new NotFoundException("This currency is not supported");
                }
            }
            String encodedOTP = RandomString.make(8);

            Payment payment = new Payment(paymentRequest.getFee(), paymentRequest.getCurrency(),
                    paymentRequest.getMethod(), paymentRequest.getIntent(),coin,formatter.format(new Date()),
                    userId,false, encodedOTP, new Date(new Date().getTime()+OTP_TIME_TRACKING),1);
            paymentService.createPayment(payment);

            emailService.sendAnEmail(user.getEmail(),VERIFICATION_CONTENT+ encodedOTP,VERIFICATION_SUBJECT);
            return ResponseEntity.ok("Check your email to verify your action.");
        }
        return ResponseEntity.badRequest().body(ACTION_UNSUCCESSFULLY);
    }

    @GetMapping("/verify_recharge/{id}")
    @PreAuthorize("hasAuthority('USER') && (authentication.principal.id == #userId)")
    @Secured("USER")
    public ResponseEntity<?> verifyRecharge(@RequestParam("code") String code,
                                              @PathVariable("id") Long userId){
        log.info("Verify recharge in Controller");

        Payment payment = paymentService.findByToken(code);
        if(payment.getExpiryTime().after(new Date())&& !payment.isSuccess()){
            User user = userService.findByID(userId);
            user.setCoin(user.getCoin()+payment.getCoin());
            userService.updateUser(user);

            payment.setSuccess(true);
            paymentService.updatePayment(payment);
            return ResponseEntity.ok(ACTION_SUCCESSFULLY);
        }
        return ResponseEntity.ok("Token is expired ");
    }

    @GetMapping("/get_OTP/{id}")
    @PreAuthorize("hasAuthority('USER') && (authentication.principal.id == #userId)")
    @Secured("USER")
    public ResponseEntity<?> getNewOTPRecharge(@PathVariable("id") Long userId){
        log.info("Get new OTP in Controller");
        Payment payment = paymentService.findByCreatedBy(userId.toString());
        if (!payment.isSuccess()
            &&payment.getNumGetToken()<MAX_FAILED_LOGIN ){
            String encodedOTP = RandomString.make(8);
            payment.setToken(encodedOTP);
            payment.setExpiryTime(new Date(new Date().getTime() + OTP_TIME_TRACKING));
            payment.setNumGetToken(payment.getNumGetToken()+1);
        }
        return ResponseEntity.ok("Check your email to verify your action.");
    }

    @GetMapping("/rewrite_password/{id}")
    @PreAuthorize("hasAuthority('USER') && (authentication.principal.id == #userId)")
    @Secured("USER")
    public ResponseEntity<?> rewriterPassword(@RequestParam("password") String password,
                                            @PathVariable("id") Long userId){
        log.info("Rewrite password in Controller");

        User saveUser = userService.findByID(userId);
        if (!new BCryptPasswordEncoder().matches(password,saveUser.getPassword())||saveUser.getDeleted()|| !saveUser.getEnabled()){
            throw new NotFoundException("Wrong password");
        }
        saveUser.setChangePassword(true);
        userService.updateUser(saveUser);
        return ResponseEntity.ok(ACTION_SUCCESSFULLY);
    }

    @GetMapping("/update_password/{id}")
    @PreAuthorize("hasAuthority('USER') && (authentication.principal.id == #userId)")
    @Secured("USER")
    public ResponseEntity<?> updatePassword(@RequestParam("password") String password,
                                            @PathVariable("id") Long userId){
        log.info("Update password in Controller");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        User saveUser = userService.findByID(userId);
        if (saveUser.getChangePassword()
                &&!new BCryptPasswordEncoder().matches(password,saveUser.getPassword())){
            String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));
            saveUser.setPassword(hash);
            saveUser.setChangePassword(false);
            saveUser.setUpdatedAt(formatter.format(new Date()));
            userService.updateUser(saveUser);
            String notificationContent = "You have change password at "+ new Date().toString();
            String notificationSubject = "Change password already";
            emailService.sendAnEmail(saveUser.getEmail(),notificationContent,notificationSubject);
            System.out.println("Update password in Controller");
            return ResponseEntity.ok(ACTION_SUCCESSFULLY);
        }
        return ResponseEntity.badRequest().body(ACTION_UNSUCCESSFULLY);
    }

}
