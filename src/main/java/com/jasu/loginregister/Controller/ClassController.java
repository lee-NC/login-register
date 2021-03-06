package com.jasu.loginregister.Controller;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Entity.DefinitionEntity.DeRole;
import com.jasu.loginregister.Filter.FilterService;
import com.jasu.loginregister.Model.Dto.BasicDto.StudentDto;
import com.jasu.loginregister.Model.Dto.BasicDto.TutorDto;
import com.jasu.loginregister.Model.Dto.ClassDto;
import com.jasu.loginregister.Model.Mapper.ClassMapper;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Model.Request.ContentFilter;
import com.jasu.loginregister.Model.Request.FilterRequest;
import com.jasu.loginregister.Model.Request.RelatedToClass.LessonRequest;
import com.jasu.loginregister.Model.Request.RelatedToClass.UpdateClassroomRequest;
import com.jasu.loginregister.Model.Request.RelatedToClass.UpdateLessonRequest;
import com.jasu.loginregister.Model.Request.UpdateToUser.DeleteAchievementSchoolRequest;
import com.jasu.loginregister.Service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.ACTION_UNSUCCESSFULLY;
import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.STATE_WAITING;

@RestController
@Slf4j
@RequestMapping("/classroom")
public class ClassController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private FilterService filterService;

    @Autowired
    private UserService userService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private UserRoleService userRoleService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    @Secured("USER")
    public ResponseEntity<?> getClassById(@PathVariable("id") Long classId, Model model) {
        log.info("Get class by Id in Controller");
        Classroom result = classroomService.findById(classId);
        ClassDto classDto = ClassMapper.toClassDto(result);
        model.addAttribute(classDto);
        User user = userService.findByID(Long.parseLong(result.getCreatedBy()));
        if (result.getUserTeachId()!=null){
            Tutor tutor = tutorService.findByUserId(Long.parseLong(result.getCreatedBy()));
            TutorDto tutorDto = UserMapper.toTutorDto(tutor,user);
            model.addAttribute(tutorDto);
        }
        else {
            Student student = studentService.findByUserId(Long.parseLong(result.getCreatedBy()));
            StudentDto studentDto = UserMapper.toStudentDto(student,user);
            model.addAttribute(studentDto);
        }
        return ResponseEntity.ok(model);
    }

    @PostMapping("/page/{pageNum}")
    public ResponseEntity<?> viewPage(Model model,
                           @PathVariable(name = "pageNum") int pageNum,
                           @Param("sortField") String sortField,
                           @Param("sortDir") String sortDir) {

        Page<Classroom> page = classroomService.listAll(pageNum, sortField, sortDir);

        List<Classroom> listProducts = page.getContent();

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listProducts", listProducts);

        return ResponseEntity.ok(model);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('USER')")
    @Secured("USER")
    public ResponseEntity<?> searchClass(@RequestParam("key") String keyWord, Model model) {
        log.info("Search class in Controller");
        List<ClassDto> classroomList = classroomService.searchClass(keyWord.toLowerCase(Locale.ROOT));
        model.addAttribute("listClass", classroomList);
        model.addAttribute("keyword",keyWord);
        return ResponseEntity.ok(model);
    }

    @PostMapping("/filter")
    @PreAuthorize("hasAnyAuthority('USER')")
    @Secured("USER")
    public ResponseEntity<?> filterClass(@Valid @RequestBody FilterRequest filterRequest) {
        log.info("Filter class in Controller");
        Set<ContentFilter> contentFilters = filterRequest.getContentFilterSet();
        if (contentFilters.isEmpty()){
            return ResponseEntity.ok("No filter");
        }
        List<ClassDto> classroomList = filterService.filterClass(contentFilters);
        return ResponseEntity.ok(classroomList);
    }

    @GetMapping("/suggest/{id}")
    @PreAuthorize("hasAnyAuthority('USER') && (authentication.principal.id == #userId)")
    @Secured("USER")
    public ResponseEntity<?> suggestClass(@PathVariable("id") Long userId, Model model) {
        log.info("Suggest class in Controller");
        User checkUser = userService.findByID(userId);
        int grade = 12;
        if (userRoleService.existUserRole(checkUser.getId(), DeRole.STUDENT.getAuthority())){
            Student student = studentService.findByUserId(userId);
            grade = student.getGrade();
        }
        List<ClassDto> classroomList = classroomService.suggestClass(checkUser,grade);
        model.addAttribute("listClass", classroomList);
        model.addAttribute("userId",userId);
        return ResponseEntity.ok(model);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('STUDENT','TUTOR') && (authentication.principal.id == #updateClassroomRequest.userCreateId)")
    @Secured({"STUDENT","TUTOR"})
    public ResponseEntity<?> updateClassInformation(@Valid @RequestBody UpdateClassroomRequest updateClassroomRequest, @PathVariable("id") Long classId){
        log.info("Update create class in Controller");

        Long userCreateId = updateClassroomRequest.getUserCreateId();

        Classroom classroom = classroomService.findById(classId);

        if (userCreateId==Long.parseLong(classroom.getCreatedBy())
            &&classroom.getState().equals(STATE_WAITING)){
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            if (updateClassroomRequest.getSubject()!=null){
                if (subjectService.existBySubjectName(updateClassroomRequest.getSubject().toUpperCase(Locale.ROOT))){
                    classroom.setSubject(updateClassroomRequest.getSubject().toUpperCase(Locale.ROOT));
                }
            }

            if (updateClassroomRequest.getMaxNum()!=0){
                classroom.setCurrentNum(updateClassroomRequest.getMaxNum());
                classroom.setMaxNum(updateClassroomRequest.getMaxNum());
            }

            if (updateClassroomRequest.getBeginDay()!=null){
                classroom.setBeginDay(formatter.format(updateClassroomRequest.getBeginDay()));
            }

            if (updateClassroomRequest.getNote()!=null){
                classroom.setNote(updateClassroomRequest.getNote());
            }

            if (updateClassroomRequest.getFee()!=0L){
                classroom.setFee(updateClassroomRequest.getFee());
            }

            if (updateClassroomRequest.getType()!=null){
                classroom.setType(updateClassroomRequest.getType());
            }

            classroomService.updateClassroom(classroom);
            classroom = classroomService.findById(classId);
            return ResponseEntity.ok(ClassMapper.toCreateClassVo(ClassMapper.toClassDto(classroom),userCreateId));
        }
        return ResponseEntity.ok(ACTION_UNSUCCESSFULLY);

    }

    @PutMapping("/lesson/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR','STUDENT')")
    @Secured({"TUTOR","STUDENT"})
    public ResponseEntity<?> updateLesson(@Valid @RequestBody UpdateLessonRequest req, @PathVariable("id") Long classId) {

        Classroom result = classroomService.findById(classId);
        for (Lesson lesson: result.getLesson()){
            if (lesson.getId()==req.getId()){
                if (req.getBeginTime()!=null&&!req.getBeginTime().equals(lesson.getBeginTime())){
                    lesson.setBeginTime(req.getBeginTime());
                }
                if (req.getEndTime()!=null&&!req.getEndTime().equals(lesson.getEndTime())){
                    lesson.setEndTime(req.getEndTime());
                }
                if (req.getDayOfWeek()!=null&&!req.getDayOfWeek().equals(lesson.getDayOfWeek())){
                    lesson.setDayOfWeek(req.getDayOfWeek().toUpperCase(Locale.ROOT));
                }

                if(!lessonService.checkExist(classId,lesson)){
                    return ResponseEntity.badRequest().body("Lesson is existed");
                }
                classroomService.updateClassroom(result);
                ClassDto classDto = ClassMapper.toClassDto(classroomService.findById(classId));
                return ResponseEntity.ok(classDto);
            }
        }
        return ResponseEntity.badRequest().body("No lesson found");
    }

    @PostMapping("/lesson/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR','STUDENT')")
    @Secured({"TUTOR","STUDENT"})
    public ResponseEntity<?> createLesson(@Valid @RequestBody LessonRequest req, @PathVariable("id") Long classId) {
        Classroom result = classroomService.findById(classId);
        Lesson lesson = new Lesson(req.getBeginTime(),req.getEndTime(),req.getDayOfWeek());
        if(!lessonService.checkExist(classId,lesson)){
            return ResponseEntity.badRequest().body("Lesson is existed");
        }
        result.getLesson().add(lesson);
        classroomService.updateClassroom(result);
        ClassDto classDto = ClassMapper.toClassDto(classroomService.findById(classId));
        return ResponseEntity.ok(classDto);
    }

    @DeleteMapping("/lesson/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR','STUDENT')")
    @Secured({"TUTOR","STUDENT"})
    public ResponseEntity<?> deleteLesson(@Valid @RequestBody DeleteAchievementSchoolRequest req, @PathVariable("id") Long classId) {
        Classroom result = classroomService.findById(classId);
        for (Lesson lesson: result.getLesson()){
            if (lesson.getId()==req.getId()){
                result.getLesson().remove(lesson);
                classroomService.updateClassroom(result);
                lessonService.deleteLesson(lesson);
                ClassDto classDto = ClassMapper.toClassDto(classroomService.findById(classId));
                return ResponseEntity.ok(classDto);
            }
        }
        return ResponseEntity.badRequest().body("No lesson found");

    }

}
