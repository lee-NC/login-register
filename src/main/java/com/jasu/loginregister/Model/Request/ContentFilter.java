package com.jasu.loginregister.Model.Request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ContentFilter {
    @NotNull(message = "field is required")
    @ApiModelProperty(
            example="3",
            notes="field cannot be empty"
    )
    private String field;

    @NotNull(message = "keyWord is required")
    @ApiModelProperty(
            example="3",
            notes="keyWord cannot be empty"
    )
    private String keyWord;
}
