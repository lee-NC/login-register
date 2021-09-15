package com.jasu.loginregister.Model.Request.RelatedToClass;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ApplyClassRequest {

    @NotNull(message = "UserId is required")
    @ApiModelProperty(
            example="3",
            notes="UserId cannot be empty",
            required=true
    )
    private Long userId;

    @NotNull(message = "ClassId is required")
    @ApiModelProperty(
            example="3",
            notes="ClassId cannot be empty",
            required=true
    )
    private Long classId;
}
