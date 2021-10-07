package com.jasu.loginregister.Model.Request.UpdateToUser;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UpdateTutorRequest {

    @ApiModelProperty(
            example="Philosopher",
            notes="Literacy cannot be empty"
    )
    private String literacy;

    @ApiModelProperty(
            example="2.5",
            notes="Fill your experience years you have"
    )
    private float experience;
}
