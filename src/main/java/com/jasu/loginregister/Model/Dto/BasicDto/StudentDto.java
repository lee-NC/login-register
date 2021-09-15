package com.jasu.loginregister.Model.Dto.BasicDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentDto {
    private UserDto userDto;

    private int grade;

    private float assessment;

    private int numAssessment;
}
