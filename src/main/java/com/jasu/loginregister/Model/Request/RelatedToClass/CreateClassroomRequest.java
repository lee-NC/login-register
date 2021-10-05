package com.jasu.loginregister.Model.Request.RelatedToClass;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
public class CreateClassroomRequest {

    @NotNull(message = "UserCreateId is required")
    @ApiModelProperty(
            example="3",
            notes="UserCreateId cannot be empty",
            required=true
    )
    private Long userCreateId;

    @NotNull(message = "Maximum Number is required")
    @ApiModelProperty(
            example="3",
            notes="Maximum Number cannot be empty",
            required=true
    )
    private int maxNum;

    @NotNull(message = "Type is required")
    @NotEmpty(message = "Type Number is required")
    @ApiModelProperty(
            example="ONLINE",
            notes="Type cannot be empty",
            required=true
    )
    private String type;

    @NotNull(message = "Subject is required")
    @NotEmpty(message = "Subject Number is required")
    @ApiModelProperty(
            example="MATHS",
            notes="Subject cannot be empty",
            required=true
    )
    private String subject;

    @Size(max = 255)
    @ApiModelProperty(
            example="Note",
            notes="Note cannot be empty"
    )
    private String note;

    @NotNull(message = "Grade is required")
    @ApiModelProperty(
            example="3",
            notes="Grade cannot be empty",
            required=true
    )
    private int grade;

    @NotNull(message = "Number Lesson is required")
    @ApiModelProperty(
            example="30",
            notes="Number Lesson cannot be empty",
            required=true
    )
    private int numLesson;


    @NotNull(message = "Price is required")
    @ApiModelProperty(
            example="300000",
            notes="Price cannot be empty",
            required=true
    )
    private Long fee;

    private Set<LessonRequest> listLesson;

    @NotNull(message = "Begin day is required")
    @ApiModelProperty(
            example="2021-08-09",
            notes="Begin day cannot be empty",
            required=true
    )
    private Date beginDay;
}
