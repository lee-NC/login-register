package com.jasu.loginregister.Model.Request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {

    @NotEmpty(message = "Email is required")
    @NotNull(message = "Email is required")
    @ApiModelProperty(
            example="abcd@gmail.com",
            notes="Email cannot be empty",
            required = true
    )
    private String email;

    @NotEmpty(message = "Password is required")
    @NotNull(message = "Password is required")
    @ApiModelProperty(
            example="1234Aasa@",
            notes="Password cannot be empty",
            required = true
    )
    private String password;
}
