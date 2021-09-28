package com.jasu.loginregister.Model.Dto.BasicDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AchievementDto {
    private Long id;
    private String achievement;
    private int year;
}
