package com.jasu.loginregister.Model.Dto.DetailDto;

import com.jasu.loginregister.Entity.Address;
import com.jasu.loginregister.Model.Dto.BasicDto.AddressDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserDetailDto {

    private Long id;

    private String fullName;

    private String gender;

    private AddressDto addressDto;

    private String birthday;

    private String phoneNumber;

    private String avatar;
}
