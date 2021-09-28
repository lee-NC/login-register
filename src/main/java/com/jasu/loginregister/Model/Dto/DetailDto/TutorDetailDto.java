package com.jasu.loginregister.Model.Dto.DetailDto;

import com.jasu.loginregister.Model.Dto.BasicDto.AchievementDto;
import com.jasu.loginregister.Model.Dto.BasicDto.SchoolDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class TutorDetailDto {

    private String literacy;

    private Set<SchoolDto> listSchool;

    private float experience;

    private Set<AchievementDto> listAchievement;
}
