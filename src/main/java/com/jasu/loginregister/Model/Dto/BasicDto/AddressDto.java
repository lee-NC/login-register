package com.jasu.loginregister.Model.Dto.BasicDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
public class AddressDto {

    private String addressDetail;

    private String ward;

    private String district;

    private String province;

    private String phoneNumber;
}
