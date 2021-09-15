package com.jasu.loginregister.Model.Dto.SignUpDto;

import com.jasu.loginregister.Model.Dto.BasicDto.AchievementDto;
import com.jasu.loginregister.Model.Dto.BasicDto.SchoolDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TutorSignUpDto {

    private UserSignUpDto userSignUpDto;

    private String literacy;

    private List<SchoolDto> listSchool;

    private float experience;

    private List<AchievementDto> listAchievement;

    private float assessment;

    private int numAssessment;
}
