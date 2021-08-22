package com.jasu.loginregister.Model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentDto {
    private UserDto userDto;

    private String literacy;

    private float assessment;

    private int numAssessment;
}
