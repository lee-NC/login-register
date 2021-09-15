package com.jasu.loginregister.Model.Request.RelatedToClass;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ApproveClassRequest {

    @NotNull(message = "UserCreatedId is required")
    @ApiModelProperty(
            example="3",
            notes="userCreatedId cannot be empty",
            required=true
    )
    private Long userCreatedId;

    @NotNull(message = "ClassId is required")
    @ApiModelProperty(
            example="3",
            notes="ClassId cannot be empty",
            required=true
    )
    private Long classId;

    @NotNull(message = "UserApprovedId is required")
    @ApiModelProperty(
            example="3",
            notes="UserApprovedId cannot be empty",
            required=true
    )
    private Long userApprovedId;
}
