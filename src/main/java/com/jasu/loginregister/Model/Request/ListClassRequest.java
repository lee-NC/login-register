package com.jasu.loginregister.Model.Request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ListClassRequest {
    @NotNull(message = "UserId is required")
    @ApiModelProperty(
            example="3",
            notes="UserId cannot be empty",
            required = true
    )
    private Long userId;

    @NotNull(message = "State is required")
    @ApiModelProperty(
            example="CREATE",
            notes="State cannot be empty",
            required = true
    )
    private String state;
}
