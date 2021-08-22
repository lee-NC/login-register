package com.jasu.loginregister.Model.Request;

import com.jasu.loginregister.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name is required")
    @ApiModelProperty(
            example="Sam Smith",
            notes="Full name cannot be empty",
            required=true
    )
    @JsonProperty("fullName")
    private String fullName;


    @NotNull(message = "Email is required")
    @NotEmpty(message = "Email is required")
    @ApiModelProperty(
            example="sam.smith@gmail.com",
            notes="Email cannot be empty",
            required=true
    )
    @Email(message = "Please provide a valid email",regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$")
    private String email;


    @NotNull(message = "Gender is required")
    @NotEmpty(message = "Gender is required")
    @Size(max = 6,min = 4, message = "Gender is female, male or other")
    @ApiModelProperty(
            example="Male",
            notes="Gender cannot be empty",
            required=true
    )
    private String gender;


    @NotNull(message = "Address is required")
    @NotEmpty(message = "Address is required")
    @Size(max= 255,min = 20, message = "Please provide a valid address")
    @ApiModelProperty(
            example="số nhà XX, đường/phố XX,phường/xã XX, quận/huyện XX, thành phố XX ",
            notes="Address cannot be empty",
            required=true
    )
    private String address;


    @NotNull(message = "Birthday is required")
    @NotEmpty(message = "Birthday is required")
    @ApiModelProperty(
            example="YYYY/MM/dd",
            notes="Birthday cannot be empty",
            required=true
    )
    private Date birthday;


    @NotNull(message = "Phone Number is required")
    @NotEmpty(message = "Phone Number is required")
    @Pattern(regexp="(0[3-9])+([0-9]{8})\\b", message = "Please provide a valid phone number")
    @ApiModelProperty(
            example="0916976012",
            notes="Phone cannot be empty",
            required=true
    )
    private String phoneNumber;


    @NotNull(message = "Password is required")
    @NotEmpty(message = "Password is required")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,30}$", message = "Passwors must be have 8 to 30 characters ," +
            " 1 lowcase character, 1 upcase character, 1 number, 1 special character ")
    @ApiModelProperty(
            example="Verysecretpassword123@",
            notes="Password can't be empty",
            required=true
    )
    private String password;


    @NotNull(message = "Role is required")
    @NotEmpty(message = "Role is required")
    @ApiModelProperty(
            example="USER",
            notes="role cannot be empty",
            required=true
    )
    private String roleKey;


    @NotNull(message = "Need to specify the creator's information")
    @NotEmpty(message = "Need to specify the creator's information")
    @ApiModelProperty(
            example="USER",
            notes="role cannot be empty",
            required=true
    )
    private String createdBy;



}
