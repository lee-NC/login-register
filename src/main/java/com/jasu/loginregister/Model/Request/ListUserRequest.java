package com.jasu.loginregister.Model.Request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ListUserRequest {

    @NotNull(message = "UserId is required")
    @ApiModelProperty(
            example="3",
            notes="UserId cannot be empty"
    )
    private Long userId;

    @NotNull(message = "ClassId is required")
    @ApiModelProperty(
            example="3",
            notes="ClassId cannot be empty"
    )
    private Long classId;

    @NotNull(message = "State is required")
    @ApiModelProperty(
            example="CREATE",
            notes="State cannot be empty"
    )
    private String state;

}
