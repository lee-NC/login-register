package com.jasu.loginregister.Model.Request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class TokenRefreshRequest {
    @NotBlank
    @NotNull(message = " Refresh Token required")
    @ApiModelProperty(
            example="3sdfasdcawfascsefcsd",
            notes="Refresh Token cannot be empty"
    )
    private String refreshToken;
}
