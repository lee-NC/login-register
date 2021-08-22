package com.jasu.loginregister.Model.Mapper;

import com.jasu.loginregister.Entity.Role;
import com.jasu.loginregister.Entity.Student;
import com.jasu.loginregister.Entity.Tutor;
import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Model.Dto.StudentDto;
import com.jasu.loginregister.Model.Dto.TutorDto;
import com.jasu.loginregister.Model.Dto.UserDto;
import com.jasu.loginregister.Model.Request.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        UserDto tmp = new UserDto();
        tmp.setId(user.getId());
        tmp.setFullName(user.getFullName());
//        tmp.setCoin(user.getCoin());
        tmp.setAvatar(user.getAvatar());
        tmp.setRoleKey(user.getRoleKey());

        return tmp;
    }

    public static TutorDto toTutorDto(Tutor tutor) {
        TutorDto tutorDto = new TutorDto();
        tutorDto.setUserDto(toUserDto(tutor.getUser()));
        tutorDto.setLiteracy(tutor.getLiteracy());
        tutorDto.setSchool(tutor.getSchool());
        tutorDto.setExperience(tutor.getExperience());
        tutorDto.setAchievement(tutor.getAchievement());
        tutorDto.setAssessment(tutor.getAssessment());
        tutorDto.setNumAssessment(tutor.getNumAssessment());
        return tutorDto;
    }

    public static StudentDto toStudentDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setUserDto(toUserDto(student.getUser()));
        studentDto.setLiteracy(student.getLiteracy());
        studentDto.setAssessment(student.getAssessment());
        studentDto.setNumAssessment(student.getNumAssessment());
        return studentDto;
    }

    public static User toUser(CreateUserRequest req) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        User user = new User();
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPhoneNumber(req.getPhoneNumber());
        user.setAddress(req.getAddress());
        user.setCoin(0L);
        user.setState("ON");
        user.setDeleted(false);
        user.setCreatedBy(req.getFullName());
        user.setCreatedAt(formatter.format(date));
        user.setLastLogin(formatter.format(date));
        user.setLastActive(formatter.format(date));
        user.setNumActive(1);
        user.setBirthday(req.getBirthday());
        user.setGender(req.getGender());
        user.setPassword(req.getPassword());
        user.setRoleKey(req.getRoleKey());
        return user;
    }

    public static Tutor toTutor(CreateTutorRequest req) {
        Tutor tutor  = new Tutor();
        User user = toUser(req.getCreateUserRequest());
        tutor.setUser(user);
        tutor.setLiteracy(req.getLiteracy());
        tutor.setSchool(req.getSchool());
        tutor.setExperience(req.getExperience());
        tutor.setAchievement(req.getAchievement());
        tutor.setAssessment(0f);
        tutor.setNumAssessment(0);
        return tutor;
    }

    public static Student toStudent(CreateStudentRequest req) {
        Student student = new Student();
        User user = toUser(req.getCreateUserRequest());
        student.setUser(user);
        student.setLiteracy(req.getLiteracy());
        student.setAssessment(0f);
        student.setNumAssessment(0);
        return student;
    }


    public static User toUser(UpdateUserRequest req, Long id) {
        User user = new User();
        user.setId(id);
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPhoneNumber(req.getPhoneNumber());
        user.setAddress(req.getAddress());
        user.setBirthday(req.getBirthday());
        user.setGender(req.getGender());
        user.setPassword(req.getPassword());

        return user;
    }


}
