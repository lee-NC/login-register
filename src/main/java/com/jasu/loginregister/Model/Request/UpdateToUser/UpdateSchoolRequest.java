package com.jasu.loginregister.Model.Request.UpdateToUser;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@Data
public class UpdateSchoolRequest {

    @NotNull(message = "school id is required")
    @ApiModelProperty(
            example="school id",
            notes="school id cannot be empty"
    )
    private Long id;
    @NotNull(message = "School name is required")
    @NotEmpty(message = "School name is required")
    @ApiModelProperty(
            example="ABC University",
            notes="School cannot be empty",
            required = true
    )
    private String schoolName;
}
