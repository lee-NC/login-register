package com.jasu.loginregister.Model.Request.UpdateToUser;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteAchievementSchoolRequest {
    @NotNull(message = "Achievement id is required")
    @ApiModelProperty(
            example="Achievement id",
            notes="Achievement id cannot be empty"
    )
    private Long id;
}
