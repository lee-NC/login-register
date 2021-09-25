package com.jasu.loginregister.Controller;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Model.Dto.BasicDto.StudentDto;
import com.jasu.loginregister.Model.Dto.BasicDto.TutorDto;
import com.jasu.loginregister.Model.Dto.ClassDto;
import com.jasu.loginregister.Model.Mapper.ClassMapper;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Model.Request.ContentFilter;
import com.jasu.loginregister.Model.Request.FilterRequest;
import com.jasu.loginregister.Model.Request.RelatedToClass.LessonRequest;
import com.jasu.loginregister.Model.Request.RelatedToClass.UpdateClassroomRequest;
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
//        List<Long> classIdList = new ArrayList<>();
//        for (ClassDto classDto: classroomList){
//            classIdList.add(classDto.getId());
//        }
//        List<ClassStatusVo> classStatusVos = new ArrayList<>();
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


//    @PutMapping("/classroom/{id}")
//    @PreAuthorize("hasAnyAuthority('USER')")
//    @Secured("USER")
//    public ResponseEntity<?> updateCreateClass(@Valid @RequestBody UpdateClassroomRequest updateClassroomRequest,, @PathVariable("id") Long classId){
//        log.info("Update create class in Controller");
//
//        Long userCreateId = updateClassroomRequest.getUserCreateId();
//        Long classId = updateClassroomRequest.getClassId();
//
//        Classroom classroom = classroomService.findById(classId);
//
//        if (userCreateId==Long.parseLong(classroom.getCreatedBy())
//                &&classroom.getUserTeachId()==null){
//            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//
//            if (updateClassroomRequest.getSubject()!=null){
//                Subject checkSubject = subjectService.checkBySubjectName(updateClassroomRequest.getSubject().toUpperCase(Locale.ROOT));
//                if (checkSubject!=null){
//                    classroom.setSubject(updateClassroomRequest.getSubject());
//                }
//            }
//
//            if (updateClassroomRequest.getMaxNum()!=0){
//                classroom.setCurrentNum(updateClassroomRequest.getMaxNum());
//                classroom.setMaxNum(updateClassroomRequest.getMaxNum());
//            }
//
//            if (updateClassroomRequest.getBeginDay()!=null){
//                classroom.setBeginDay(formatter.format(updateClassroomRequest.getBeginDay()));
//            }
//
//            if (updateClassroomRequest.getNumLesson()!=0){
//                classroom.setNumLesson(updateClassroomRequest.getNumLesson());
//            }
//
//            if (updateClassroomRequest.getNote()!=null){
//                classroom.setNote(updateClassroomRequest.getNote());
//            }
//
//            if (updateClassroomRequest.getFee()!=0L){
//                classroom.setFee(updateClassroomRequest.getFee());
//            }
//
//            if (updateClassroomRequest.getType()!=null){
//                classroom.setType(updateClassroomRequest.getType());
//            }
//
//            if (!updateClassroomRequest.getListLesson().isEmpty()){
//                List<Lesson> lessonList = classroom.getLesson();
//                classroom.setLesson(updateLesson(lessonList,updateClassroomRequest));
//            }
//            Classroom updateClassroom = classroomService.updateClassroom(classroom);
//            if (updateClassroom!=null){
//                return ResponseEntity.ok(ClassMapper.toCreateClassVo(ClassMapper.toClassDto(updateClassroom),userCreateId));
//            }
//        }
//        return ResponseEntity.ok("Have some wrong, try it later");
//
//    }

//    //check all field in an object are empty
//    private Boolean checkEmpty(Lesson lesson) {
//        return Stream.of(lesson.getBeginTime(), lesson.getEndTime(),lesson.getDayOfWeek())
//                .allMatch(Objects::isNull);
//    }

//    //update list lesson
//    private List<Lesson> updateLesson(List<Lesson> lessonList, UpdateClassroomRequest updateClassroomRequest){
//        int count =0;
//        if (lessonList.size()<=updateClassroomRequest.getListLesson().size()){
//            count = lessonList.size();
//
//            for(int i=count;i<updateClassroomRequest.getListLesson().size();i++){
//                String beginTime = updateClassroomRequest.getListLesson().get(i).getBeginTime();
//                String endTime = updateClassroomRequest.getListLesson().get(i).getEndTime();
//                String dayOfWeek = updateClassroomRequest.getListLesson().get(i).getDayOfWeek();
//                Lesson lesson = new Lesson(beginTime,endTime,dayOfWeek);
//                if (!checkEmpty(lesson)){
//                    lessonList.add(lesson);
//                }
//            }
//        }
//        else {
//            count = updateClassroomRequest.getListLesson().size();
//            List<Lesson> lessonRemove = lessonList.subList(count,lessonList.size());
//            lessonList.removeAll(lessonRemove);
//        }
//        for (int i=0;i<count;i++){
//            if (updateClassroomRequest.getListLesson().get(i).getBeginTime()!=null){
//                lessonList.get(i).setBeginTime(updateClassroomRequest.getListLesson().get(i).getBeginTime());
//            }
//            if (updateClassroomRequest.getListLesson().get(i).getEndTime()!=null){
//                lessonList.get(i).setBeginTime(updateClassroomRequest.getListLesson().get(i).getEndTime());
//            }
//            if (updateClassroomRequest.getListLesson().get(i).getDayOfWeek()!=null){
//                lessonList.get(i).setBeginTime(updateClassroomRequest.getListLesson().get(i).getDayOfWeek());
//            }
//        }
//        return lessonList;
//    }


}
