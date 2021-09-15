package com.jasu.loginregister.Model.Request.UpdateToUser;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
public class UpdateStudentRequest {

    @Max(value = 12)
    @Min(value = 1)
    @ApiModelProperty(
            example="Grade 8",
            notes="grade cannot be empty"
    )
    private int grade;
}
