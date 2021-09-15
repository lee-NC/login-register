package com.jasu.loginregister.Model.Request.CreatedToUser;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentRequest {

    @NotNull(message = "UserId is required")
    @ApiModelProperty(
            example= " 8",
            notes="UserId cannot be empty",
            required=true
    )
    private Long userId;

    @NotNull(message = "Grade is required")
    @Max(value = 12)
    @Min(value = 1)
    @ApiModelProperty(
            example="Grade 8",
            notes="Grade cannot be empty",
            required=true
    )
    private int grade;
}
