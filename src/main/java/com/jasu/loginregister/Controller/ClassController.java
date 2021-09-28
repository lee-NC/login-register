package com.jasu.loginregister.Controller;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Entity.DefinitionEntity.DeRole;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Model.Dto.BasicDto.StudentDto;
import com.jasu.loginregister.Model.Dto.BasicDto.TutorDto;
import com.jasu.loginregister.Model.Dto.ClassDto;
import com.jasu.loginregister.Model.Mapper.ClassMapper;
import com.jasu.loginregister.Model.Mapper.UserDetailMapper;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Model.Request.ContentFilter;
import com.jasu.loginregister.Model.Request.CreatedToUser.SchoolRequest;
import com.jasu.loginregister.Model.Request.FilterRequest;
import com.jasu.loginregister.Model.Request.RelatedToClass.LessonRequest;
import com.jasu.loginregister.Model.Request.RelatedToClass.UpdateClassroomRequest;
import com.jasu.loginregister.Model.Request.RelatedToClass.UpdateLessonRequest;
import com.jasu.loginregister.Model.Request.UpdateToUser.DeleteAchievementSchoolRequest;
import com.jasu.loginregister.Model.Request.UpdateToUser.UpdateSchoolRequest;
import com.jasu.loginregister.Service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.ACTION_UNSUCCESSFULLY;

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

    @GetMapping("/filter")
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

    @PutMapping("/classroom/{id}")
    @PreAuthorize("hasAnyAuthority('STUDENT','TUTOR')")
    @Secured({"STUDENT","TUTOR"})
    public ResponseEntity<?> updateCreateClass(@Valid @RequestBody UpdateClassroomRequest updateClassroomRequest, @PathVariable("id") Long classId){
        log.info("Update create class in Controller");

        Long userCreateId = updateClassroomRequest.getUserCreateId();

        Classroom classroom = classroomService.findById(classId);

        if (userCreateId==Long.parseLong(classroom.getCreatedBy())
                &&classroom.getUserTeachId()==null){
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            if (updateClassroomRequest.getSubject()!=null){
                if (subjectService.existBySubjectName(updateClassroomRequest.getSubject().toUpperCase(Locale.ROOT))){
                    classroom.setSubject(updateClassroomRequest.getSubject());
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
            Classroom updateClassroom = classroomService.findById(classId);
            return ResponseEntity.ok(ClassMapper.toCreateClassVo(ClassMapper.toClassDto(updateClassroom),userCreateId));
        }
        return ResponseEntity.ok(ACTION_UNSUCCESSFULLY);

    }

    @PutMapping("/lesson/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR','STUDENT')")
    @Secured({"TUTOR","STUDENT"})
    public ResponseEntity<?> updateLessonTutor(@Valid @RequestBody UpdateLessonRequest req, @PathVariable("id") Long classId) {

        Classroom result = classroomService.findById(classId);
        Lesson lesson = lessonService.findById(req.getId());
        if (req.getBeginTime()!=null){
            lesson.setBeginTime(req.getBeginTime());
        }
        if (req.getEndTime()!=null){
            lesson.setEndTime(req.getEndTime());
        }
        if (req.getDayOfWeek()!=null){
            lesson.setDayOfWeek(req.getDayOfWeek());
        }
        if (lessonService.existedByBeginTimeAndEndTimeAndDayOfWeek(req.getBeginTime(),req.getEndTime(),req.getDayOfWeek())){
            return ResponseEntity.badRequest().body(new DuplicateFormatFlagsException("Achievement was existed"));
        }
        lessonService.updateLesson(lesson);
        result = classroomService.findById(classId);
        ClassDto classDto = ClassMapper.toClassDto(result);
        return ResponseEntity.ok(classDto);
    }

    @PostMapping("/lesson/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR','STUDENT')")
    @Secured({"TUTOR","STUDENT"})
    public ResponseEntity<?> createlessonTutor(@Valid @RequestBody LessonRequest req, @PathVariable("id") Long classId) {
        Lesson lesson = new Lesson(req.getBeginTime(),req.getEndTime(),req.getDayOfWeek());
        if (lessonService.existedByBeginTimeAndEndTimeAndDayOfWeek(req.getBeginTime(),req.getEndTime(),req.getDayOfWeek())){
            return ResponseEntity.badRequest().body(new DuplicateFormatFlagsException("Achievement was existed"));
        }
        lessonService.createLesson(lesson);
        Classroom result = classroomService.findById(classId);
        ClassDto classDto = ClassMapper.toClassDto(result);
        return ResponseEntity.ok(classDto);
    }

    @DeleteMapping("/lesson/{id}")
    @PreAuthorize("hasAnyAuthority('TUTOR','STUDENT')")
    @Secured({"TUTOR","STUDENT"})
    public ResponseEntity<?> deleteSchoolTutor(@Valid @RequestBody DeleteAchievementSchoolRequest req, @PathVariable("id") Long classId) {
        Lesson lesson = lessonService.findById(req.getId());
        lessonService.deleteLesson(lesson);
        Classroom result = classroomService.findById(classId);
        ClassDto classDto = ClassMapper.toClassDto(result);
        return ResponseEntity.ok(classDto);
    }

}
