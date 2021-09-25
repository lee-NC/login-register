package com.jasu.loginregister.Model.Request.RelatedToClass;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonRequest {

    @NotNull(message = "BeginTime is required")
    @NotEmpty(message = "BeginTime is required")
    @Size(max = 6,min = 4)
    @ApiModelProperty(
            example="9:00",
            notes="BeginTime cannot be empty",
            required=true
    )
    private String beginTime;

    @NotNull(message = "endTime is required")
    @NotEmpty(message = "endTime is required")
    @Size(max = 6,min = 4)
    @ApiModelProperty(
            example="9:00",
            notes="endTime cannot be empty",
            required=true
    )
    private String endTime;


    @NotNull(message = "dayOfWeek is required")
    @NotEmpty(message = "dayOfWeek is required")
    @Size(max = 15,min = 6)
    @ApiModelProperty(
            example="Friday",
            notes="dayOfWeek cannot be empty",
            required=true
    )
    private String dayOfWeek;
}
