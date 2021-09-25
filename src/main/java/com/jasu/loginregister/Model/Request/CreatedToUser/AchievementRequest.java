package com.jasu.loginregister.Model.Request.CreatedToUser;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
public class AchievementRequest {

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
