package com.jasu.loginregister.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    private String fullName;

    private String gender;

    private String address;

    private Date birthday;

    private String phoneNumber;

    @Email(message = "Please provide a valid email")
    private String email;

    @Size(min = 8, max = 30, message = "Password must be between 8 and 20 characters")
    private String password;

    private String avatar;
}
