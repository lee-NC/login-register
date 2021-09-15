package com.jasu.loginregister.Model.Dto.BasicDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;

@Data
@NoArgsConstructor
public class TutorDto {

    private UserDto userDto;

    private String literacy;

    private List<SchoolDto> listSchool;

    private float experience;

    private List<AchievementDto> listAchievement;

    private float assessment;

    private int numAssessment;
}
