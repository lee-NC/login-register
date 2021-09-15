package com.jasu.loginregister.Controller;

import com.jasu.loginregister.Entity.Classroom;
import com.jasu.loginregister.Entity.Lesson;
import com.jasu.loginregister.Entity.Subject;
import com.jasu.loginregister.Model.Dto.ClassDto;
import com.jasu.loginregister.Model.Mapper.ClassMapper;
import com.jasu.loginregister.Model.Request.RelatedToClass.LessonRequest;
import com.jasu.loginregister.Model.Request.RelatedToClass.UpdateClassroomRequest;
import com.jasu.loginregister.Service.ClassroomService;
import com.jasu.loginregister.Service.LessonService;
import com.jasu.loginregister.Service.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
@Slf4j
@RequestMapping("/classroom")
public class ClassController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> getClassById(@PathVariable("id") Long classId) {
        log.info("Get class by Id in Controller");
        Classroom result = classroomService.findById(classId);
        return ResponseEntity.ok(ClassMapper.toClassDto(result));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('STUDENT','TUTOR')")
    public ResponseEntity<?> updateCreateClass(@Valid @RequestBody UpdateClassroomRequest updateClassroomRequest){
        log.info("Update create class in Controller");

        Long userCreateId = updateClassroomRequest.getUserCreateId();
        Long classId = updateClassroomRequest.getClassId();

        Classroom classroom = classroomService.findById(classId);

        if (userCreateId==Long.parseLong(classroom.getCreatedBy())
                &&classroom.getUserTeachId()==null){
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            if (updateClassroomRequest.getSubject()!=null){
                Subject checkSubject = subjectService.checkBySubjectName(updateClassroomRequest.getSubject().toUpperCase(Locale.ROOT));
                if (checkSubject!=null){
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

            if (updateClassroomRequest.getNumLesson()!=0){
                classroom.setNumLesson(updateClassroomRequest.getNumLesson());
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

            if (!updateClassroomRequest.getListLesson().isEmpty()){
                List<Lesson> lessonList = classroom.getLesson();
                classroom.setLesson(updateLesson(lessonList,updateClassroomRequest));
            }
            Classroom updateClassroom = classroomService.updateClassroom(classroom);
            if (updateClassroom!=null){
                return ResponseEntity.ok(ClassMapper.toCreateClassVo(ClassMapper.toClassDto(updateClassroom),userCreateId));
            }
        }
        return ResponseEntity.ok("Have some wrong, try it later");

    }

    //check all field in an object are empty
    private Boolean checkEmpty(Lesson lesson) {
        return Stream.of(lesson.getBeginTime(), lesson.getEndTime(),lesson.getDayOfWeek())
                .allMatch(Objects::isNull);
    }

    //update list lesson
    private List<Lesson> updateLesson(List<Lesson> lessonList, UpdateClassroomRequest updateClassroomRequest){
        int count =0;
        if (lessonList.size()<=updateClassroomRequest.getListLesson().size()){
            count = lessonList.size();

            for(int i=count;i<updateClassroomRequest.getListLesson().size();i++){
                String beginTime = updateClassroomRequest.getListLesson().get(i).getBeginTime();
                String endTime = updateClassroomRequest.getListLesson().get(i).getEndTime();
                String dayOfWeek = updateClassroomRequest.getListLesson().get(i).getDayOfWeek();
                Lesson lesson = new Lesson(beginTime,endTime,dayOfWeek);
                if (!checkEmpty(lesson)){
                    lessonList.add(lesson);
                }
            }
        }
        else {
            count = updateClassroomRequest.getListLesson().size();
            List<Lesson> lessonRemove = lessonList.subList(count,lessonList.size());
            lessonList.removeAll(lessonRemove);
        }
        for (int i=0;i<count;i++){
            if (updateClassroomRequest.getListLesson().get(i).getBeginTime()!=null){
                lessonList.get(i).setBeginTime(updateClassroomRequest.getListLesson().get(i).getBeginTime());
            }
            if (updateClassroomRequest.getListLesson().get(i).getEndTime()!=null){
                lessonList.get(i).setBeginTime(updateClassroomRequest.getListLesson().get(i).getEndTime());
            }
            if (updateClassroomRequest.getListLesson().get(i).getDayOfWeek()!=null){
                lessonList.get(i).setBeginTime(updateClassroomRequest.getListLesson().get(i).getDayOfWeek());
            }
        }
        return lessonList;
    }

}
