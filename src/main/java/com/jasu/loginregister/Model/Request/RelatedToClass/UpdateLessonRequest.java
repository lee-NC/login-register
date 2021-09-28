package com.jasu.loginregister.Model.Request.RelatedToClass;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLessonRequest {

    @NotNull(message = "Id is required")
    @ApiModelProperty(
            example="3",
            notes="id cannot be empty",
            required = true
    )
    private Long id;

    @Size(max = 6,min = 4)
    @ApiModelProperty(
            example="9:00",
            notes="BeginTime cannot be empty"
    )
    private String beginTime;


    @Size(max = 6,min = 4)
    @ApiModelProperty(
            example="9:00",
            notes="endTime cannot be empty"
    )
    private String endTime;



    @Size(max = 15,min = 6)
    @ApiModelProperty(
            example="Friday",
            notes="dayOfWeek cannot be empty"
    )
    private String dayOfWeek;
}
