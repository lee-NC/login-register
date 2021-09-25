package com.jasu.loginregister.Controller;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Entity.DefinitionEntity.DeRole;
import com.jasu.loginregister.Exception.ErrorResponse;
import com.jasu.loginregister.Model.Dto.BasicDto.StudentDto;
import com.jasu.loginregister.Model.Dto.ClassDto;
import com.jasu.loginregister.Model.Mapper.ClassMapper;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Model.Request.ListClassRequest;
import com.jasu.loginregister.Model.Request.ListUserRequest;
import com.jasu.loginregister.Model.Request.RelatedToClass.ApproveClassRequest;
import com.jasu.loginregister.Model.Request.RelatedToClass.CreateClassroomRequest;
import com.jasu.loginregister.Model.Request.RelatedToClass.ApplyClassRequest;
import com.jasu.loginregister.Model.Request.UpdateToUser.UpdateTutorRequest;
import com.jasu.loginregister.Model.Request.UpdateToUser.UpdateUserRequest;
import com.jasu.loginregister.Model.ValueObject.ClassStatusVo;
import com.jasu.loginregister.Service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.*;

@RestController
@Slf4j
@RequestMapping("/tutor")
public class TutorController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private ClassTutorService classTutorService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClassStudentService classStudentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private TutorStudentSerivce tutorStudentSerivce;

    @PostMapping("/create_class")
    @PreAuthorize("hasAuthority('TUTOR')")
    @Secured("STUDENT")
    public ResponseEntity<?> tutorCreateClass(@Valid @RequestBody CreateClassroomRequest createClassroomRequest){
        log.info("Tutor create class in Controller");

        Long userCreatedId = createClassroomRequest.getUserCreateId();

        Boolean checkTimes = classTutorService.checkRecentClassTutor(userCreatedId,STATE_CREATE);

        Subject checkSubject = subjectService.checkBySubjectName(createClassroomRequest.getSubject());

        if (checkTimes && checkSubject!=null){
            ClassDto classDto = classroomService.createClassroom(createClassroomRequest, DeRole.TUTOR.getAuthority(), userCreatedId);
            classTutorService.createClassroomTutor(userCreatedId,classDto.getId(),STATE_CREATE);
            return ResponseEntity.ok(ClassMapper.toCreateClassVo(classDto,userCreatedId));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST,ACTION_UNSUCCESSFULLY));
    }

    @PostMapping("/apply")
    @PreAuthorize("hasAuthority('TUTOR')")
    @Secured("STUDENT")
    public ResponseEntity<?> tutorApplyClass(@Valid @RequestBody ApplyClassRequest applyClassRequest) {
        log.info("Tutor apply class in Controller");

        Long userApplyId = applyClassRequest.getUserId();
        User checkUser = userService.findByID(userApplyId);
        Long classId = applyClassRequest.getClassId();
        Classroom classroom = classroomService.findById(classId);

        //lay 20% tien phi 1 buoi hoc chia cho so nguoi tham gia cho 1 lan dang ki
        Long fee = ((classroom.getFee()/20)*classroom.getMaxNum())/100;
        if (checkUser.getCoin()<fee){
            return ResponseEntity.badRequest().body("There is not enough coin in the account right now");
        }

        Boolean checkTimes = classTutorService.checkRecentClassTutor(userApplyId,STATE_APPLY);
        if (classroom.getUserTeachId()==null
                &&classroom.getState().equals(STATE_WAITING)
                &&userApplyId!=Long.parseLong(classroom.getCreatedBy())
                &&checkTimes){

            if (!classTutorService.existByClassIdAndUserId(classId,userApplyId)){
                classTutorService.createClassroomTutor(userApplyId,classId,STATE_APPLY);
                checkUser.setCoin(checkUser.getCoin()-fee);
                userService.updateUser(checkUser);
                return ResponseEntity.ok(ACTION_APPLY_SUCCESSFUL);
            }
            else {
                ClassTutor checkClassTutor = classTutorService.findByClassIdAndUserId(classId,userApplyId);
                if (checkClassTutor.getState().equals(STATE_APPLY)){
                    return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST,ACTION_APPLY_BEFORE));
                }
                if (checkClassTutor.getState().equals(STATE_CANCELED)){
                    checkClassTutor.setState(STATE_APPLY);
                    Boolean updateClassTutor= classTutorService.updateClassroomTutor(checkClassTutor);
                    if (updateClassTutor){
                        checkUser.setCoin(checkUser.getCoin()-fee);
                        userService.updateUser(checkUser);
                        return ResponseEntity.ok(ACTION_APPLY_SUCCESSFUL);
                    }
                }
            }
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST,ACTION_UNSUCCESSFULLY));
    }

    @PostMapping("/approve")
    @PreAuthorize("hasAuthority('TUTOR')")
    @Secured("STUDENT")
    public ResponseEntity<?> tutorApproveClass(@Valid @RequestBody ApproveClassRequest approveClassRequest){
        log.info("Tutor approve a student for a class in Controller");

        Long userCreatedId = approveClassRequest.getUserCreatedId();
        Long userApprovedId = approveClassRequest.getUserApprovedId();
        Long classId = approveClassRequest.getClassId();

        ClassStudent checkClassStudent = classStudentService.findByClassIdAndUserId(classId,userApprovedId);

        if (checkClassStudent==null||!checkClassStudent.getState().equals(STATE_APPLY)){
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST,ACTION_APPLY_NOT_FOUND));
        }

        Classroom classroom = classroomService.findById(classId);
        Boolean checkRole = userRoleService.existUserRole(userApprovedId, DeRole.STUDENT.getAuthority());

        if (classroom.getUserTeachId().equals(userCreatedId)
                &&!userCreatedId.equals(userApprovedId)
//                &&classroom.getCreatedBy().equals(userCreatedId.toString()) //dieu kien co the bo
                &&checkRole
                &&checkClassStudent.getState().equals(STATE_APPLY)
                &&classroom.getState().equals(STATE_WAITING)){

            classroom.setCurrentNum(classroom.getCurrentNum()+1);
            classroomService.updateClassroom(classroom);

            if (classroom.getCurrentNum()<classroom.getMaxNum()){
                checkClassStudent.setState(STATE_APPROVED);
                if(classStudentService.updateClassroomStudent(checkClassStudent)){
                    return ResponseEntity.ok("You approved a student in this class. Classroom will start in begin day");
                }
            }

            if (classroom.getCurrentNum()==classroom.getMaxNum()){

                classroom.setState(STATE_PROCESSING);
                classroomService.updateClassroom(classroom);

                checkClassStudent.setState(STATE_PROCESSING);
                ClassTutor classTutor = classTutorService.findByClassIdAndUserId(classId,userCreatedId);
                classTutor.setState(STATE_PROCESSING);

                if(classTutorService.updateClassroomTutor(classTutor)
                        &&classStudentService.updateClassroomStudent(checkClassStudent)
                        &&classStudentService.updateListStudentInClassroom(classId,STATE_APPLY,STATE_REJECTED)
                        &&classStudentService.updateListStudentInClassroom(classId,STATE_APPROVED,STATE_PROCESSING)){
                    //lay 20% tien phi 1 buoi hoc chia cho so nguoi tham gia cho 1 lan dang ki
                    Long fee = ((classroom.getFee()/20)*classroom.getMaxNum())/100;
                    List <Long> studentIds = classStudentService.getListUserIDByClassIdAndState(classId,STATE_PROCESSING);
                    List<Long> studentBeRejectedId = classStudentService.getListUserIDByClassIdAndState(classId,STATE_REJECTED);
                    if (tutorStudentSerivce.createListStudentService(classId,userCreatedId,studentIds)
                        &&userService.refundUserBeRejected(studentBeRejectedId,fee)) {

                        return ResponseEntity.ok("Class is starting, check your class");
                    }
                }
            }
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST,ACTION_UNSUCCESSFULLY));
    }

    @PostMapping("/cancel_apply")
    @PreAuthorize("hasAuthority('TUTOR')")
    @Secured("STUDENT")
    public ResponseEntity<?> tutorCancelApplyClass(@Valid @RequestBody ApplyClassRequest applyClassRequest) {
        log.info("Tutor cancel sign up class in Controller");

        Long userApplyId = applyClassRequest.getUserId();
        Long classId = applyClassRequest.getClassId();
        ClassTutor checkClassTutor = classTutorService.findByClassIdAndUserId(classId,userApplyId);

        if (checkClassTutor==null||checkClassTutor.getState().equals(STATE_CANCELED)){
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST,ACTION_APPLY_NOT_FOUND));
        }

        Classroom classroom = classroomService.findById(classId);

        if (classroom.getUserTeachId()==null
                &&checkClassTutor.getState().equals(STATE_APPLY)
                &&classroom.getState().equals(STATE_WAITING)
                &&userApplyId!=Long.parseLong(classroom.getCreatedBy())){
            checkClassTutor.setState(STATE_CANCELED);
            if (classTutorService.updateClassroomTutor(checkClassTutor)){
                return ResponseEntity.ok(ACTION_CANCEL_APPLY);
            }
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST,ACTION_UNSUCCESSFULLY));
    }

    @PostMapping("/like_class")
    @PreAuthorize("hasAuthority('TUTOR')")
    @Secured("TUTOR")
    public ResponseEntity<?> likeClass(@RequestBody ApplyClassRequest applyClassRequest) {
        log.info("Tutor like class in Controller");
        Long userId = applyClassRequest.getUserId();
        Long classId = applyClassRequest.getClassId();
        ClassTutor classTutor = classTutorService.findByClassIdAndUserId(classId,userId);
        if (!classTutor.getLikeClass()){
            classTutor.setLikeClass(true);
            classTutorService.updateClassroomTutor(classTutor);
            return ResponseEntity.ok(ACTION_SUCCESSFULLY);
        }
        return ResponseEntity.status(HttpStatus.OK).body("You like it before");
    }

    @PostMapping("/dislike_class")
    @PreAuthorize("hasAuthority('TUTOR')")
    @Secured("TUTOR")
    public ResponseEntity<?> dislikeClass(@RequestBody ApplyClassRequest applyClassRequest) {
        log.info("Tutor dislike class in Controller");
        Long userId = applyClassRequest.getUserId();
        Long classId = applyClassRequest.getClassId();
        ClassTutor classTutor = classTutorService.findByClassIdAndUserId(classId,userId);
        if (classTutor.getLikeClass()){
            classTutor.setLikeClass(false);
            classTutorService.updateClassroomTutor(classTutor);
            return ResponseEntity.ok(ACTION_SUCCESSFULLY);
        }
        return ResponseEntity.status(HttpStatus.OK).body("You dislike it before");
    }

    @PostMapping("/list_class")
    @PreAuthorize("hasAuthority('USER')")
    @Secured("USER")
    public ResponseEntity<?> getClassApplyByUserId(@RequestBody ListClassRequest listClassRequest) {
        log.info("get list class tutor in Controller");
        Long userId = listClassRequest.getUserId();
        String state = listClassRequest.getState().toUpperCase(Locale.ROOT);
        List<ClassTutor> classTutors = classTutorService.getListClassTutorByUserIdAndState(userId,state);
        List<Long> classIds = new ArrayList<Long>();
        for (ClassTutor classTutor:classTutors) {
            classIds.add(classTutor.getClassroomCtId());
        }
        List<ClassDto> classDtos = classroomService.getListClass(classIds);
        List<ClassStatusVo> result = new ArrayList<>();
        for (int i = 0;i< classIds.size();i++){
            ClassStatusVo classStatusVo = new ClassStatusVo(
                    classDtos.get(i),classTutors.get(i).getLikeClass());
            result.add(classStatusVo);
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/list_student")
    @PreAuthorize("hasAuthority('TUTOR')")
    @Secured("TUTOR")
    public ResponseEntity<?> getStudentInAClassByClassId(@RequestBody ListUserRequest listUserRequest) {
        log.info("get list class tutor sign up in Controller");

        Long classId = listUserRequest.getClassId();
        Long userId = listUserRequest.getUserId();

        Classroom classroom = classroomService.findById(classId);
        if (userId.equals(classroom.getUserTeachId())) {
            String state = classroom.getState();
            List<Long> userIds = new ArrayList<>();

            if (state.equals(STATE_WAITING)){
                userIds = classStudentService.getListUserIDByClassIdAndState(classId, listUserRequest.getState().toUpperCase(Locale.ROOT));
            }
            else {
                userIds = classStudentService.getListUserIDByClassIdAndState(classId,state);
            }

            List<User> users = userService.getListUser(userIds);
            List<Student>students = studentService.getByListUserId(userIds);
            int count = 0;

            List<StudentDto> result = new ArrayList<>();
            while(count<users.size()){
                result.add(UserMapper.toStudentDto(students.get(count),users.get(count)));
                count++;
            }
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.ok("No student found");

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Secured("USER")
    public ResponseEntity<?> getTutorById(@PathVariable("id") Long userId) {
        log.info("get tutor by Id in Controller");
        Tutor tutor = tutorService.findByUserId(userId);
        User user = userService.findByID(userId);
        return ResponseEntity.ok(UserMapper.toTutorDetailDto(tutor,user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TUTOR')")
    @Secured("STUDENT")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest req, @PathVariable("id") Long id) {
        User user = userService.updateDetailUser(req, id);
        Tutor updateTutor = null;
        if (req.getUpdateTutorRequest()!=null){
            Tutor tutor = tutorService.findByUserId(id);
            updateTutor = tutorService.updateTutor(updateTutor(tutor,req.getUpdateTutorRequest()));
        }
        return ResponseEntity.ok(UserMapper.toTutorDetailDto(updateTutor,user));
    }

    private Tutor updateTutor(Tutor checkTutor, UpdateTutorRequest updateTutorRequest){

        if (updateTutorRequest.getExperience()!=0f){
            checkTutor.setExperience(updateTutorRequest.getExperience());
        }

        if (updateTutorRequest.getLiteracy()!=null){
            checkTutor.setLiteracy(updateTutorRequest.getLiteracy());
        }

        if (updateTutorRequest.getListTutorAchievement()!=null){
            List<Achievement> achievementList= checkTutor.getAchievements();
            checkTutor.setAchievements(updateListAchievement(achievementList,updateTutorRequest));
        }

        if (updateTutorRequest.getListTutorSchool()!=null){
            List<School> schoolList = checkTutor.getSchools();
            checkTutor.setSchools(updateListSchool(schoolList,updateTutorRequest));
        }
        return checkTutor;
    }

    private List<Achievement> updateListAchievement(List<Achievement>achievementList, UpdateTutorRequest updateTutorRequest){
        int count =0;
        if (achievementList.size()<=updateTutorRequest.getListTutorAchievement().size()){
            count = achievementList.size();
            for (int i=count;i<updateTutorRequest.getListTutorAchievement().size();i++){
                if (updateTutorRequest.getListTutorAchievement().get(i).getAchievement()!=null
                        &&updateTutorRequest.getListTutorAchievement().get(i).getYear()>YEAR_ACHIEVEMENT){
                    Achievement achievement = new Achievement(updateTutorRequest.getListTutorAchievement().get(i).getAchievement()
                            ,updateTutorRequest.getListTutorAchievement().get(i).getYear());
                    achievementList.add(achievement);
                }
            }
        }
        else  {
            count = updateTutorRequest.getListTutorAchievement().size();
            for (int i=count;i<updateTutorRequest.getListTutorAchievement().size();i++){
                achievementList.remove(i);
            }
        }
        for (int i = 0;i<count;i++){
            if (updateTutorRequest.getListTutorAchievement().get(i).getAchievement()!=null){
                achievementList.get(i).setAchievement(updateTutorRequest.getListTutorAchievement().get(i).getAchievement());
            }
            if (updateTutorRequest.getListTutorAchievement().get(i).getYear()!=0){
                achievementList.get(i).setYear(updateTutorRequest.getListTutorAchievement().get(i).getYear());
            }
        }
        return achievementList;
    }

    private List<School> updateListSchool(List<School> schoolList,UpdateTutorRequest updateTutorRequest){
        int count =0;
        if (schoolList.size()<=updateTutorRequest.getListTutorSchool().size()){
            count = schoolList.size();
            for (int i=count;i<updateTutorRequest.getListTutorSchool().size();i++){
                if (updateTutorRequest.getListTutorSchool().get(i).getSchoolName()!=null){
                    School school = new School(updateTutorRequest.getListTutorSchool().get(i).getSchoolName());
                    schoolList.add(school);

                }
            }
        }
        else {
            count = updateTutorRequest.getListTutorSchool().size();
            for (int i=count;i<updateTutorRequest.getListTutorSchool().size();i++){
                schoolList.remove(i);
            }
        }
        for (int i = 0;i<count;i++){
            if (updateTutorRequest.getListTutorSchool().get(i).getSchoolName()!=null){
                schoolList.get(i).setSchoolName(updateTutorRequest.getListTutorSchool().get(i).getSchoolName());
            }
        }
        return schoolList;
    }

}
