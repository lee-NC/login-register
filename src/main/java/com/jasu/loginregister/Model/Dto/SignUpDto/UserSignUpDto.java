package com.jasu.loginregister.Model.Dto.SignUpDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSignUpDto {

    private String fullName;

    private String avatar;

    private String gender;
}
