package com.jasu.loginregister.Model.Request.UpdateToUser;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UpdateAchievementRequest {

    @NotNull(message = "Achievement id is required")
    @ApiModelProperty(
            example="Achievement id",
            notes="Achievement id cannot be empty"
    )
    private Long id;

    @ApiModelProperty(
            example="Achievement",
            notes="Achievement cannot be empty"
    )
    private String achievement;

    @Min(value = 1970)
    @ApiModelProperty(
            example="1989",
            notes="Year cannot be empty"
    )
    private int year;
}
