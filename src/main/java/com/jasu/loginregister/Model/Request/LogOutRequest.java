package com.jasu.loginregister.Model.Request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LogOutRequest {

    @NotNull(message = "UserId is required")
    @ApiModelProperty(
            example="3",
            notes="UserId cannot be empty"
    )
    private Long userId;
}
