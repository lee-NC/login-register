package com.jasu.loginregister.Model.Request.CreatedToUser;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressRequest {


    @NotNull(message = "addressDetail is required")
    @NotEmpty(message = "addressDetail is required")
    @Size(max = 25)
    @ApiModelProperty(
            example="SN XX",
            notes="Password can't be empty",
            required=true
    )
    private String addressDetail;

    @NotNull(message = "ward is required")
    @NotEmpty(message = "ward is required")
    @Size(max = 45)
    @ApiModelProperty(
            example="Cau Dien",
            notes="ward can't be empty"
    )
    private String ward;

    @NotNull(message = "district is required")
    @NotEmpty(message = "district is required")
    @Size(max = 25)
    @ApiModelProperty(
            example="Cau Giay",
            notes="district can't be empty",
            required=true
    )
    private String district;

    @NotNull(message = "province is required")
    @NotEmpty(message = "province is required")
    @Size(max = 25)
    @ApiModelProperty(
            example="Ha Noi",
            notes="province can't be empty",
            required=true
    )
    private String province;

    @NotNull(message = "Phone Number is required")
    @NotEmpty(message = "Phone Number is required")
    @Pattern(regexp="(0[3-9])+([0-9]{8})\\b", message = "Please provide a valid phone number")
    @ApiModelProperty(
            example="0916976012",
            notes="Phone cannot be empty",
            required=true
    )
    private String phoneNumber;
}
