package com.jasu.loginregister.Model.Mapper;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Model.Dto.BasicDto.TutorDto;
import com.jasu.loginregister.Model.Dto.ClassDto;
import com.jasu.loginregister.Model.Request.RelatedToClass.CreateClassroomRequest;
import com.jasu.loginregister.Model.Request.RelatedToClass.LessonRequest;
import com.jasu.loginregister.Model.ValueObject.CreateClassVo;

import java.text.SimpleDateFormat;
import java.util.*;

public class ClassMapper {

    public static Classroom toClass(CreateClassroomRequest req,String roleKey, Long userCreateId) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
        Set<Lesson> lessonList = new HashSet<>();
        for (LessonRequest lessonRequest : req.getListLesson()){
            Lesson lesson = new Lesson(lessonRequest.getBeginTime(),lessonRequest.getEndTime(),lessonRequest.getDayOfWeek().toUpperCase(Locale.ROOT));
            lessonList.add(lesson);
        }
        Date date = new Date();
        Classroom classroom = new Classroom();
        if (roleKey.equals("TUTOR")){
            classroom.setUserTeachId(userCreateId);
        }
        else classroom.setUserTeachId(null);
        if(roleKey.equals("STUDENT")){
            classroom.setCurrentNum(req.getMaxNum());
        }
        else    classroom.setCurrentNum(0);
        classroom.setMaxNum(req.getMaxNum());
        classroom.setBeginDay(formatterDate.format(req.getBeginDay()));
        classroom.setSubject(req.getSubject().toUpperCase(Locale.ROOT));
        classroom.setGrade(req.getGrade());
        classroom.setLesson(lessonList);
        classroom.setNumLesson(req.getNumLesson());
        classroom.setState("WAITING");
        classroom.setType(req.getType());
        classroom.setDeleted(false);
        classroom.setCreatedAt(formatter.format(date));
        classroom.setCreatedBy(userCreateId.toString());
        classroom.setFee(req.getFee());
        return classroom;

    }

    public static ClassTutor toClassTutor(Long tutorId,Long id,String state) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        ClassTutor classTutor = new ClassTutor();
        classTutor.setUserCtId(tutorId);
        classTutor.setClassroomCtId(id);
        classTutor.setState(state);
        classTutor.setLikeClass(false);
        classTutor.setCreatedAt(formatter.format(date));
        return classTutor;

    }

    public static ClassStudent toClassStudent( Long studentId, Long classId,String state) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        ClassStudent classStudent = new ClassStudent();
        classStudent.setUserCsId(studentId);
        classStudent.setLikeClass(false);
        classStudent.setClassroomCsId(classId);
        classStudent.setState(state);
        classStudent.setCreatedAt(formatter.format(date));
        return classStudent;

    }

    public static ClassDto toClassDto(Classroom classroom) {
        ClassDto classDto = new ClassDto();
        classDto.setId(classroom.getId());
        classDto.setGrade(classroom.getGrade());
        classDto.setBeginDay(classroom.getBeginDay());
        classDto.setNumLesson(classroom.getNumLesson());
        classDto.setCurrentNum(classroom.getCurrentNum());
        classDto.setMaxNum(classroom.getMaxNum());
        classDto.setType(classroom.getType());
        classDto.setFee(classroom.getFee());
        classDto.setSubject(classroom.getSubject());
        classDto.setLessonList(classroom.getLesson());

        return classDto;
    }

    public static CreateClassVo toCreateClassVo(ClassDto classDto, Long userCreateId){
        CreateClassVo createClassVo = new CreateClassVo();
        createClassVo.setClassDto(classDto);
        createClassVo.setUserCreateId(userCreateId);
        return createClassVo;
    }
}
