package com.jasu.loginregister.Model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TutorDto {

    private UserDto userDto;

    private String literacy;

    private String school;

    private String experience;

    private String achievement;

    private float assessment;

    private int numAssessment;
}
