package com.jasu.loginregister.Model.Mapper;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Model.Dto.BasicDto.*;
import com.jasu.loginregister.Model.Dto.DetailDto.StudentDetailDto;
import com.jasu.loginregister.Model.Dto.DetailDto.TutorDetailDto;
import com.jasu.loginregister.Model.Dto.DetailDto.UserDetailDto;
import com.jasu.loginregister.Jwt.Principal.UserPrincipal;
import com.jasu.loginregister.Model.Request.CreatedToUser.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.text.SimpleDateFormat;
import java.util.*;

public class UserMapper {

    public static UserPrincipal toUserPrincipal(User user) {
        UserPrincipal tmp = new UserPrincipal();
        tmp.setId(user.getId());
        tmp.setFullName(user.getFullName());
        tmp.setCoin(user.getCoin());
        tmp.setAvatar(user.getAvatar());
        tmp.setNumActive(user.getNumActive());
        return tmp;
    }

    public static UserDto toUserDto(User user) {
        UserDto tmp = new UserDto();
        tmp.setId(user.getId());
        tmp.setFullName(user.getFullName());
        tmp.setAvatar(user.getAvatar());
        return tmp;
    }

    public static TutorDto toTutorDto(Tutor tutor,User user) {
        List<AchievementDto> achievementDtos= new ArrayList<>();
        for (Achievement achievement : tutor.getAchievements()){
            AchievementDto achievementDto = new AchievementDto(achievement.getAchievement(), achievement.getYear());
            achievementDtos.add(achievementDto);
        }
        List<SchoolDto> schoolDtos = new ArrayList<>();
        for (School school : tutor.getSchools()){
            SchoolDto schoolDto = new SchoolDto(school.getSchoolName());
            schoolDtos.add(schoolDto);
        }
        TutorDto tutorDto = new TutorDto();
        tutorDto.setUserDto(toUserDto(user));
        tutorDto.setLiteracy(tutor.getLiteracy());
        tutorDto.setListAchievement(achievementDtos);
        tutorDto.setListSchool(schoolDtos);
        tutorDto.setExperience(tutor.getExperience());
        tutorDto.setAssessment(tutor.getAssessment());
        tutorDto.setNumAssessment(tutor.getNumAssessment());
        return tutorDto;
    }

    public static StudentDto toStudentDto(Student student,User user) {
        StudentDto studentDto = new StudentDto();
        studentDto.setUserDto(toUserDto(user));
        studentDto.setGrade(student.getGrade());
        studentDto.setAssessment(student.getAssessment());
        studentDto.setNumAssessment(student.getNumAssessment());
        return studentDto;
    }

    public static User toUser(CreateUserRequest req) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        User user = new User();
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPhoneNumber(req.getPhoneNumber());
        user.setAddress(req.getAddress());
        user.setCoin(0L);
        user.setState("LOGIN");
        user.setDeleted(false);
        user.setCreatedAt(formatter.format(date));
        user.setLastLogin(formatter.format(date));
        user.setLastActive(formatter.format(date));
        user.setNumActive(1);
        user.setCreatedBy(req.getEmail());
        user.setBirthday(formatterDate.format(req.getBirthday()));
        user.setGender(req.getGender().toUpperCase(Locale.ROOT));
        Set<DeRole> roles = new HashSet<>();
        roles.add(DeRole.USER);
//        user.setRoles(roles);
        String hash = BCrypt.hashpw(req.getPassword(), BCrypt.gensalt(12));
        user.setPassword(hash);
        return user;
    }

    public static Tutor toTutor(CreateTutorRequest req) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Tutor tutor  = new Tutor();
        tutor.setUserTutorId(req.getUserId());
        tutor.setLiteracy(req.getLiteracy());
        tutor.setCreatedAt(formatter.format(date));
        tutor.setExperience(req.getExperience());
        tutor.setDeleted(false);
        tutor.setAssessment(0f);
        tutor.setCreatedBy(req.getUserId().toString());
        tutor.setNumAssessment(0);
        List<Achievement> achievements = new ArrayList<>();
        for (AchievementRequest createTutorAchievementRequest : req.getListTutorAchievement()){
            Achievement achievement = new Achievement(tutor,createTutorAchievementRequest.getAchievement(),createTutorAchievementRequest.getYear());
            achievements.add(achievement);
        }
        tutor.setAchievements(achievements);
        List<School> schools = new ArrayList<>();
        for (SchoolRequest schoolRequest : req.getListTutorSchool()){
            School school = new School(tutor,schoolRequest.getSchoolName());
            schools.add(school);
        }
        tutor.setSchools(schools);
        return tutor;
    }

    public static Student toStudent(CreateStudentRequest req) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Student student = new Student();
        student.setUserStudentId(req.getUserId());
        student.setGrade(req.getGrade());
        student.setAssessment(0f);
        student.setNumAssessment(0);
        student.setCreatedBy(req.getUserId().toString());
        student.setCreatedAt(formatter.format(date));
        student.setDeleted(false);
        return student;
    }

    public static UserDetailDto toUserDetailDto(User user) {
        UserDetailDto userDetailDto = new UserDetailDto();
        userDetailDto.setFullName(user.getFullName());
        userDetailDto.setAddress(user.getAddress());
        userDetailDto.setBirthday(user.getBirthday());
        userDetailDto.setAvatar(user.getAvatar());
        userDetailDto.setGender(user.getGender());
        userDetailDto.setPhoneNumber(user.getPhoneNumber());

        return userDetailDto;
    }

    public static TutorDetailDto toTutorDetailDto(Tutor tutor,User user) {
        TutorDetailDto tutorDetailDto = new TutorDetailDto();
        tutorDetailDto.setLiteracy(tutor.getLiteracy());
        tutorDetailDto.setListAchievement(toAchievementDto(tutor.getAchievements()));
        tutorDetailDto.setListSchool(toSchoolDto(tutor.getSchools()));
        tutorDetailDto.setExperience(tutorDetailDto.getExperience());
        tutorDetailDto.setUserDetailDto(toUserDetailDto(user));
        return tutorDetailDto;
    }

    private static List<SchoolDto> toSchoolDto(List<School> schools) {
        List<SchoolDto> schoolDtoList = new ArrayList<>();
        for (School school: schools){
            SchoolDto schoolDto = new SchoolDto(school.getSchoolName());
            schoolDtoList.add(schoolDto);
        }
        return schoolDtoList;
    }

    private static List<AchievementDto> toAchievementDto(List<Achievement> achievements) {
        List<AchievementDto> achievementDtoList = new ArrayList<>();
        for (Achievement achievement: achievements){
            AchievementDto achievementDto = new AchievementDto(achievement.getAchievement(),achievement.getYear());
            achievementDtoList.add(achievementDto);
        }
        return achievementDtoList;
    }

    public static StudentDetailDto toStudentDetailDto(Student student,User user) {
        StudentDetailDto studentDetailDto = new StudentDetailDto();
        studentDetailDto.setGrade(student.getGrade());
        studentDetailDto.setUserDetailDto(toUserDetailDto(user));
        return studentDetailDto;
    }

    public static TutorStudent toTutorStudent(Long classId, Long tutorId, Long studentId){
        TutorStudent tutorStudent = new TutorStudent();
        tutorStudent.setClassTsId(classId);
        tutorStudent.setUserTutorTsId(tutorId);
        tutorStudent.setUserStudentTsId(studentId);
        tutorStudent.setStudentAssessment(0);
        tutorStudent.setStudentLike(false);
        tutorStudent.setTutorAssessment(0);
        tutorStudent.setTutorLike(false);
        tutorStudent.setIsInvite(false);
        tutorStudent.setIsCancel(false);
        tutorStudent.setClassInviteId(0L);

        return tutorStudent;
    }

}
