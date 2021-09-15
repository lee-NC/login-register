package com.jasu.loginregister.Model.Dto.DetailDto;

import com.jasu.loginregister.Model.Dto.BasicDto.AchievementDto;
import com.jasu.loginregister.Model.Dto.BasicDto.SchoolDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;

@Data
@NoArgsConstructor
public class TutorDetailDto {

    private UserDetailDto userDetailDto;

    private String literacy;

    private List<SchoolDto> listSchool;

    private String experience;

    private List<AchievementDto> listAchievement;
}
