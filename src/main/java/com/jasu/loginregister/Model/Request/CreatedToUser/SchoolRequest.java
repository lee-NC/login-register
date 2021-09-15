package com.jasu.loginregister.Model.Request.CreatedToUser;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SchoolRequest {

    @NotNull(message = "School name is required")
    @NotEmpty(message = "School name is required")
    @ApiModelProperty(
            example="ABC University",
            notes="School cannot be empty",
            required = true
    )
    private String schoolName;
}
