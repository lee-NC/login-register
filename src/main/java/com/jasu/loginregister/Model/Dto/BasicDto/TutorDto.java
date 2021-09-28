package com.jasu.loginregister.Model.Dto.BasicDto;

import com.jasu.loginregister.Entity.Achievement;
import com.jasu.loginregister.Entity.School;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class TutorDto {

    private UserDto userDto;

    private String literacy;

    private Set<SchoolDto> listSchool;

    private float experience;

    private Set<AchievementDto> listAchievement;

    private float assessment;

    private int numAssessment;
}
