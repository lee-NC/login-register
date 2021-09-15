package com.jasu.loginregister.Controller;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Exception.ErrorResponse;
import com.jasu.loginregister.Model.Dto.BasicDto.TutorDto;
import com.jasu.loginregister.Model.Dto.ClassDto;
import com.jasu.loginregister.Model.Mapper.ClassMapper;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Model.Request.ListClassRequest;
import com.jasu.loginregister.Model.Request.ListUserRequest;
import com.jasu.loginregister.Model.Request.RelatedToClass.*;
import com.jasu.loginregister.Model.Request.UpdateToUser.UpdateUserRequest;
import com.jasu.loginregister.Model.ValueObject.ClassStatusVo;
import com.jasu.loginregister.Service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.jasu.loginregister.Entity.DefineEntityStateMessage.*;

@RestController
@Slf4j
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private ClassStudentService classStudentService;

    @Autowired
    private ClassTutorService classTutorService;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private TutorStudentSerivce tutorStudentSerivce;

    @PostMapping("/create_class")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Secured("STUDENT")
    public ResponseEntity<?> studentCreateClass(@Valid @RequestBody CreateClassroomRequest createClassroomRequest){
        log.info("Student create class in Controller");

        Long userCreateId = createClassroomRequest.getUserCreateId();

        Student student = studentService.findByUserId(userCreateId);

        Boolean checkTimes = classStudentService.checkRecentClassStudent(userCreateId,STATE_CREATE);

        Subject checkSubject = subjectService.checkBySubjectName(createClassroomRequest.getSubject());

        if (checkTimes && checkSubject!=null
                && student.getGrade() == createClassroomRequest.getGrade()){
            ClassDto classDto = classroomService.createClassroom(createClassroomRequest, DeRole.STUDENT.getAuthority(), userCreateId);
            ClassStudent classStudent = classStudentService.createClassroomStudent(userCreateId,classDto.getId(),STATE_CREATE);
            return ResponseEntity.ok(ClassMapper.toCreateClassVo(classDto,userCreateId));
        }
        return ResponseEntity.ok(ACTION_UNSUCCESSFULLY);

    }

    @PostMapping("/apply")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Secured("STUDENT")
    public ResponseEntity<?> studentApplyClass(@Valid @RequestBody ApplyClassRequest applyClassRequest){
        log.info("Student apply a class in Controller");

        Long userApplyId = applyClassRequest.getUserId();
        Student student = studentService.findByUserId(userApplyId);

        Long classId = applyClassRequest.getClassId();
        Classroom classroom = classroomService.findById(classId);

        Boolean checkTimes = classStudentService.checkRecentClassStudent(userApplyId,STATE_APPLY);

        if (classroom.getUserTeachId().equals(Long.parseLong(classroom.getCreatedBy()))
                &&checkTimes
                &&userApplyId.equals(classroom.getUserTeachId())
                &&student.getGrade()>=classroom.getGrade()
                &&classroom.getState().equals(STATE_WAITING)){

            if (classStudentService.findByClassIdAndUserId(classId,userApplyId)==null){
                classStudentService.createClassroomStudent(userApplyId,classId, STATE_APPLY);
            }
            else{
                ClassStudent checkClassStudent = classStudentService.findByClassIdAndUserId(classId,userApplyId);
                if (checkClassStudent.getState().equals(STATE_APPLY)){
                    return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST,ACTION_APPLY_BEFORE));
                }
                if (checkClassStudent.getState().equals(STATE_CANCELED)){
                    checkClassStudent.setState(STATE_APPLY);
                    Boolean updateClassStudent= classStudentService.updateClassroomStudent(checkClassStudent);
                }
            }
            return ResponseEntity.ok(ACTION_APPLY_SUCCESSFUL);
        }

        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST,ACTION_UNSUCCESSFULLY));

    }

    @PostMapping("/approve")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Secured("STUDENT")
    public ResponseEntity<?> studentApproveClass(@Valid @RequestBody ApproveClassRequest approveClassRequest){
        log.info("Student approve a tutor for a class in Controller");

        Long userCreatedId = approveClassRequest.getUserCreatedId();
        Long userApprovedId = approveClassRequest.getUserApprovedId();
        Long classId = approveClassRequest.getClassId();

        ClassTutor checkClassTutor = classTutorService.findByClassIdAndUserId(classId,userApprovedId);

        if (checkClassTutor==null||!checkClassTutor.getState().equals(STATE_APPLY)){
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST,ACTION_APPLY_NOT_FOUND));
        }

        Classroom classroom = classroomService.findById(classId);

        if (userCreatedId.equals(Long.parseLong(classroom.getCreatedBy()))
                &&userCreatedId.equals(userApprovedId)
                &&userRoleService.existUserRole(userApprovedId, DeRole.TUTOR.getAuthority())
                &&classroom.getState().equals(STATE_WAITING)
                &&checkClassTutor.getState().equals(STATE_APPLY)
                &&classroom.getUserTeachId()==null){

            classroom.setState(STATE_PROCESSING);
            checkClassTutor.setState(STATE_PROCESSING);
            classroom.setUserTeachId(approveClassRequest.getUserApprovedId());
            List <Long> studentIds = new ArrayList<>();
            studentIds.add(userCreatedId);
            ClassStudent classStudent = classStudentService.findByClassIdAndUserId(classId,userCreatedId);
            classStudent.setState(STATE_PROCESSING);
            if(classStudentService.updateClassroomStudent(classStudent)
                    &&classTutorService.updateClassroomTutor(checkClassTutor)
                    &&classTutorService.rejectAllClassroomTutor(classId,STATE_APPLY)
                    &&classroomService.updateClassroom(classroom)!=null
                    &&tutorStudentSerivce.createListStudentService(classId,userApprovedId,studentIds)){

                return ResponseEntity.ok("Class is starting, check your class");
            }
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST,ACTION_UNSUCCESSFULLY));

    }

    @PostMapping("/cancel_apply")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Secured("STUDENT")
    public ResponseEntity<?> studentCancelApplyClass(@Valid @RequestBody ApplyClassRequest applyClassRequest){
        log.info("Student cancel sign up a class in Controller");

        Long userApplyId = applyClassRequest.getUserId();
        Long classId = applyClassRequest.getClassId();

        ClassStudent classStudent = classStudentService.findByClassIdAndUserId(classId,userApplyId);

        if (classStudent==null||!classStudent.getState().equals(STATE_APPLY)){
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST,ACTION_APPLY_NOT_FOUND));
        }

        Classroom classroom = classroomService.findById(classId);

        if (userApplyId!=Long.parseLong(classroom.getCreatedBy())
                &&classroom.getState().equals(STATE_WAITING)
                &&classStudent.getState().equals(STATE_APPLY)){
            classroom.setCurrentNum(classroom.getCurrentNum()-1);
            Classroom updateClassroom = classroomService.updateClassroom(classroom);
            classStudent.setState(STATE_CANCELED);
            if (classStudentService.updateClassroomStudent(classStudent)
                &&updateClassroom!=null){

                return ResponseEntity.ok(ACTION_CANCEL_APPLY);
            }
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST,ACTION_UNSUCCESSFULLY));
    }

    @PostMapping("/like_class")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Secured("STUDENT")
    public ResponseEntity<?> likeClass(@RequestBody ApplyClassRequest applyClassRequest) {
        log.info("Student like class in Controller");
        Long userId = applyClassRequest.getUserId();
        Long classId = applyClassRequest.getClassId();
        ClassStudent classStudent = classStudentService.findByClassIdAndUserId(classId,userId);
        if (!classStudent.getLikeClass()){
            classStudent.setLikeClass(true);
            classStudentService.updateClassroomStudent(classStudent);
            return ResponseEntity.ok(ACTION_SUCCESSFULLY);
        }

        return ResponseEntity.status(HttpStatus.OK).body("You like it before");
    }

    @PostMapping("/dislike_class")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Secured("STUDENT")
    public ResponseEntity<?> dislikeClass(@RequestBody ApplyClassRequest applyClassRequest) {
        log.info("Student dislike class in Controller");
        Long userId = applyClassRequest.getUserId();
        Long classId = applyClassRequest.getClassId();
        ClassStudent classStudent = classStudentService.findByClassIdAndUserId(classId,userId);
        if (classStudent.getLikeClass()){
            classStudent.setLikeClass(false);
            classStudentService.updateClassroomStudent(classStudent);
            return ResponseEntity.ok(ACTION_SUCCESSFULLY);
        }

        return ResponseEntity.ok("You dislike it before");
    }

    @PostMapping("/list_class")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Secured("STUDENT")
    public ResponseEntity<?> getClassByUserIdAndState(@RequestBody ListClassRequest listClassRequest) {
        log.info("get list class student sign up in Controller");
        Long userId = listClassRequest.getUserId();
        String state = listClassRequest.getState().toUpperCase(Locale.ROOT);
        List<ClassStudent> classStudents = classStudentService.getListClassStudentByUserIdAndState(userId,state);
        List<Long> classIds = new ArrayList<>();
        for (ClassStudent classStudent:classStudents) {
            classIds.add(classStudent.getClassroomCsId());
        }
        List<ClassDto> classDtos = classroomService.getListClass(classIds);

        List<ClassStatusVo> result = new ArrayList<>();
        for (int i = 0;i< classIds.size();i++){
            ClassStatusVo classStatusVo = new ClassStatusVo(
                    classDtos.get(i),classStudents.get(i).getLikeClass());
            result.add(classStatusVo);
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @PostMapping("/list_tutor")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Secured("STUDENT")
    public ResponseEntity<?> getTutorApplyClassByClassId(@RequestBody ListUserRequest listUserRequest) {
        log.info("Get list class student sign up in Controller");

        Long classId = listUserRequest.getClassId();
        Long userId = listUserRequest.getUserId();

        Classroom classroom = classroomService.findById(classId);
        if (userId.equals(Long.parseLong(classroom.getCreatedBy()))){
            String state = classroom.getState();
            List<Long> userIds = new ArrayList<>();
            if (state.equals(STATE_WAITING)){
                userIds = classTutorService.getListUserID(classId, STATE_APPLY);
            }
            else {
                userIds = classTutorService.getListUserID(classId,state);
            }

            List<User> users = userService.getListUser(userIds);
            List<Tutor> tutors = tutorService.getByListUserId(userIds);

            int count = 0;
            List<TutorDto> result = new ArrayList<>();
            while(count<users.size()){
                result.add(UserMapper.toTutorDto(tutors.get(count),users.get(count)));
                count++;
            }
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.ok("No tutor found");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Secured("STUDENT")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long userId) {
        log.info("get student by Id in Controller");
        User user = userService.findByID(userId);
        Student student = studentService.findByUserId(userId);
        return ResponseEntity.ok(UserMapper.toStudentDetailDto(student,user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Secured("STUDENT")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest req, @PathVariable("id") Long id) {
        User user = userService.updateUser(req, id);
        Student updateStudent = null;
        if (req.getUpdateStudentRequest()!=null){
            Student student = studentService.findByUserId(id);
            if (req.getUpdateStudentRequest().getGrade()>0&&req.getUpdateStudentRequest().getGrade()<13){
                student.setGrade(req.getUpdateStudentRequest().getGrade());
            }
            updateStudent = studentService.updateStudent(student);
        }
        return ResponseEntity.ok(UserMapper.toStudentDetailDto(updateStudent,user));
    }


}
