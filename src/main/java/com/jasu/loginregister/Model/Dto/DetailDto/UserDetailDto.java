package com.jasu.loginregister.Model.Dto.DetailDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserDetailDto {

    private String fullName;

    private String gender;

    private String address;

    private String birthday;

    private String phoneNumber;

    private String avatar;
}
