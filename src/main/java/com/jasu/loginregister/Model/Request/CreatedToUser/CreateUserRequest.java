package com.jasu.loginregister.Model.Request.CreatedToUser;

import com.jasu.loginregister.Entity.Address;
import com.jasu.loginregister.Model.Request.CreateAddressRequest;
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

    @NotNull(message = "Full name is required")
    @NotEmpty(message = "Full name is required")
    @ApiModelProperty(
            example="Sam Smith",
            notes="Full name cannot be empty",
            required=true
    )
    @JsonProperty("fullName")
    private String fullName;


    @NotNull(message = "Email is required")
    @NotEmpty(message = "Email is required")
    @Email(message = "Please provide a valid email")
    @ApiModelProperty(
            example="sam.smith@gmail.com",
            notes="Email cannot be empty",
            required=true
    )
    @Email(message = "Please provide a valid email",regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
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
    private CreateAddressRequest createAddressRequest;


    @ApiModelProperty(
            example="YYYY-MM-dd",
            notes="Birthday cannot be empty",
            required=true
    )
    private Date birthday;


    @NotNull(message = "Password is required")
    @NotEmpty(message = "Password is required")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,30}$",
            message = "Passwors must be have 8 to 30 characters, " +
                    "1 lowcase character, 1 upcase character, 1 number, 1 special character ")
    @ApiModelProperty(
            example="Verysecretpassword123@",
            notes="Password can't be empty",
            required=true
    )
    private String password;

}
