package com.jasu.loginregister.Model.Mapper;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Model.Dto.BasicDto.AchievementDto;
import com.jasu.loginregister.Model.Dto.BasicDto.SchoolDto;
import com.jasu.loginregister.Model.Dto.SignUpDto.StudentSignUpDto;
import com.jasu.loginregister.Model.Dto.SignUpDto.TutorSignUpDto;
import com.jasu.loginregister.Model.Dto.SignUpDto.UserSignUpDto;

import java.util.ArrayList;
import java.util.List;

public class UserSignUpMapper {
    public static TutorSignUpDto toTutorSignUpDto(Tutor tutor,User user) {
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
        TutorSignUpDto tutorDto = new TutorSignUpDto();
        tutorDto.setUserSignUpDto(toUserSignUpDto(user));
        tutorDto.setLiteracy(tutor.getLiteracy());
        tutorDto.setListAchievement(achievementDtos);
        tutorDto.setListSchool(schoolDtos);
        tutorDto.setExperience(tutor.getExperience());
        tutorDto.setAssessment(tutor.getAssessment());
        tutorDto.setNumAssessment(tutor.getNumAssessment());
        return tutorDto;
    }

    private static UserSignUpDto toUserSignUpDto(User user) {
        UserSignUpDto tmp = new UserSignUpDto();
        tmp.setFullName(user.getFullName());
        tmp.setAvatar(user.getAvatar());
        return tmp;
    }

    public static StudentSignUpDto toStudentSignUpDto(Student student,User user) {
        StudentSignUpDto studentSignUpDto = new StudentSignUpDto();
        studentSignUpDto.setUserSignUpDto(toUserSignUpDto(user));
        studentSignUpDto.setGrade(student.getGrade());
        studentSignUpDto.setAssessment(student.getAssessment());
        studentSignUpDto.setNumAssessment(student.getNumAssessment());
        return studentSignUpDto;
    }

}
