package com.jasu.loginregister.Controller;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Entity.DefinitionEntity.DeRole;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Model.Mapper.UserDetailMapper;
import com.jasu.loginregister.Model.Request.CreatedToUser.AchievementRequest;
import com.jasu.loginregister.Model.Request.CreatedToUser.SchoolRequest;
import com.jasu.loginregister.Model.Request.UpdateToUser.*;
import com.jasu.loginregister.Service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.net.MalformedURLException;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.YEAR_ACHIEVEMENT;

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


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('STUDENT','TUTOR')")
    @Secured({"STUDENT","TUTOR"})
    public ResponseEntity<?> getUserById(@PathVariable("id") Long userId) {
        log.info("get student by Id in Controller");
        User user = userService.findByID(userId);
        Student student = null;
        Tutor tutor = null;
        if (userRoleService.existUserRole(userId, DeRole.STUDENT.getAuthority())) {
            student = studentService.findByUserId(userId);
        }
        if (userRoleService.existUserRole(userId, DeRole.TUTOR.getAuthority())) {
            tutor = tutorService.findByUserId(userId);
        }
        return ResponseEntity.ok(UserDetailMapper.toUserDetailDto(user,student,tutor));
    }

    @GetMapping("/avatar/{filename}")
    public ResponseEntity<?> download(@PathVariable String filename) {
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
    @PreAuthorize("hasAnyAuthority('STUDENT','TUTOR')")
    @Secured({"STUDENT","TUTOR"})
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest req, @PathVariable("id") Long id) {
        User user = userService.updateDetailUser(req, id);
        Student updateStudent = null;
        Tutor updateTutor = null;
        if (userRoleService.existUserRole(id, DeRole.STUDENT.getAuthority())){
            if (req.getUpdateStudentRequest()!=null){
                Student student = studentService.findByUserId(id);
                if (req.getUpdateStudentRequest().getGrade()>0&&req.getUpdateStudentRequest().getGrade()<13
                        &&req.getUpdateStudentRequest().getGrade()!=student.getGrade()){
                    student.setGrade(req.getUpdateStudentRequest().getGrade());
                }
                updateStudent = studentService.updateStudent(student);
            }
        }
        if (userRoleService.existUserRole(id, DeRole.TUTOR.getAuthority())){

            if (req.getUpdateTutorRequest()!=null){
                Tutor tutor = tutorService.findByUserId(id);
                updateTutor = tutorService.updateTutor(updateTutor(tutor,req.getUpdateTutorRequest()));
            }
        }
        return ResponseEntity.ok(UserDetailMapper.toUserDetailDto(user,updateStudent,updateTutor));
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
    @PreAuthorize("hasAnyAuthority('TUTOR')")
    @Secured({"TUTOR"})
    public ResponseEntity<?> updateAchievementTutor(@Valid @RequestBody UpdateAchievementRequest req,
                                                    @PathVariable("id") Long userId) {
        log.info("update achievement in Controller");
        User user = userService.findByID(userId);
        Student student = null;
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
        if (userRoleService.existUserRole(userId, DeRole.STUDENT.getAuthority())) {
            student = studentService.findByUserId(userId);
        }

        return ResponseEntity.ok(UserDetailMapper.toUserDetailDto(user,student,tutor));
    }

    @PostMapping("/achievement/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR')")
    @Secured({"TUTOR"})
    public ResponseEntity<?> createAchievementTutor(@Valid @RequestBody AchievementRequest req,
                                                    @PathVariable("id") Long userId) {
        log.info("create achievement in Controller");
        User user = userService.findByID(userId);
        Student student = null;
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
        if (userRoleService.existUserRole(userId, DeRole.STUDENT.getAuthority())) {
            student = studentService.findByUserId(userId);
        }
        return ResponseEntity.ok(UserDetailMapper.toUserDetailDto(user,student,tutor));
    }

    @DeleteMapping("/achievement/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR')")
    @Secured({"TUTOR"})
    public ResponseEntity<?> deleteAchievementTutor(@Valid @RequestBody DeleteAchievementSchoolRequest req,
                                                    @PathVariable("id") Long userId) {
        log.info("Delete achievement in Controller");
        User user = userService.findByID(userId);
        Student student = null;
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
        if (userRoleService.existUserRole(userId, DeRole.STUDENT.getAuthority())) {
            student = studentService.findByUserId(userId);
        }
        return ResponseEntity.ok(UserDetailMapper.toUserDetailDto(user,student,tutor));
    }

    @PutMapping("/school/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR')")
    @Secured({"TUTOR"})
    public ResponseEntity<?> updateSchoolTutor(@Valid @RequestBody UpdateSchoolRequest req,
                                               @PathVariable("id") Long userId) {
        log.info("updateSchool in Controller");
        User user = userService.findByID(userId);
        Student student = null;
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
        if (userRoleService.existUserRole(userId, DeRole.STUDENT.getAuthority())) {
            student = studentService.findByUserId(userId);
        }
        return ResponseEntity.ok(UserDetailMapper.toUserDetailDto(user,student,tutor));
    }

    @PostMapping("/school/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR')")
    @Secured({"TUTOR"})
    public ResponseEntity<?> createSchoolTutor(@Valid @RequestBody SchoolRequest req,
                                               @PathVariable("id") Long userId) {
        log.info("createSchool in Controller");
        User user = userService.findByID(userId);
        Student student = null;
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
        if (userRoleService.existUserRole(userId, DeRole.STUDENT.getAuthority())) {
            student = studentService.findByUserId(userId);
        }
        return ResponseEntity.ok(UserDetailMapper.toUserDetailDto(user,student,tutor));
    }

    @DeleteMapping("/school/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR')")
    @Secured({"TUTOR"})
    public ResponseEntity<?> deleteSchoolTutor(@Valid @RequestBody DeleteAchievementSchoolRequest req,
                                               @PathVariable("id") Long userId) {
        log.info("Delete School in Controller");
        User user = userService.findByID(userId);
        Student student = null;
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
        if (userRoleService.existUserRole(userId, DeRole.STUDENT.getAuthority())) {
            student = studentService.findByUserId(userId);
        }
        return ResponseEntity.ok(UserDetailMapper.toUserDetailDto(user,student,tutor));
    }
}
