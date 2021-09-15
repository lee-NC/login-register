package com.jasu.loginregister.Model.Dto.SignUpDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentSignUpDto {
    
    private UserSignUpDto userSignUpDto;

    private int grade;

    private float assessment;

    private int numAssessment;

}
