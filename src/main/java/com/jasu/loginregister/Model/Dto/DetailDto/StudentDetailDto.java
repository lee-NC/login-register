package com.jasu.loginregister.Model.Dto.DetailDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentDetailDto {

    private  UserDetailDto userDetailDto;

    private int grade;
}
