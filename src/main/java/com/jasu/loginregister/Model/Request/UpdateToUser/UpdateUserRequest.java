package com.jasu.loginregister.Model.Request.UpdateToUser;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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


    @Size(max= 255,min = 20, message = "Please provide a valid address")
    @ApiModelProperty(
            example="số nhà XX, đường/phố XX,phường/xã XX, quận/huyện XX, thành phố XX ",
            notes="Address cannot be empty"
    )
    private String address;

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

    private UpdateTutorRequest updateTutorRequest;

    private UpdateStudentRequest updateStudentRequest;

}
