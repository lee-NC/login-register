package com.jasu.loginregister.Model.Request.UpdateToUser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jasu.loginregister.Model.Request.CreatedToUser.CreateAddressRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    @ApiModelProperty(
            example="Sam Smith",
            notes="Full name cannot be empty"
    )
    @JsonProperty("full_name")
    private String fullName;


    @Size(max = 6,min = 4, message = "Gender is female, male or other")
    @ApiModelProperty(
            example="Male",
            notes="Gender cannot be empty"
    )
    private String gender;

    private CreateAddressRequest createAddressRequest;

    @ApiModelProperty(
            example="YYYY-MM-dd",
            notes="Birthday cannot be empty"
    )
    private Date birthday;


    @Pattern(regexp="(0[3-9])+([0-9]{8})\\b", message = "Please provide a valid phone number")
    @ApiModelProperty(
            example="0916976012",
            notes="Phone cannot be empty"
    )
    private String phoneNumber;


    private MultipartFile avatar;

}
