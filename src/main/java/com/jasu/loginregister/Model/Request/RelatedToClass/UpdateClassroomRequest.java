package com.jasu.loginregister.Model.Request.RelatedToClass;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class UpdateClassroomRequest {

    @NotNull(message = "UserCreateId is required ")
    @ApiModelProperty(
            example="3",
            notes="UserCreateId cannot be empty"
    )
    private Long userCreateId;

    @NotNull(message = "ClassId is required ")
    @ApiModelProperty(
            example="3",
            notes="ClassId cannot be empty"
    )
    private Long classId;

    @ApiModelProperty(
            example="3",
            notes="Maximum Number cannot be empty"
    )
    private int maxNum;

    @ApiModelProperty(
            example="ONLINE",
            notes="Type cannot be empty"
    )
    private String type;

    @ApiModelProperty(
            example="MATHS",
            notes="Subject cannot be empty"
    )
    private String subject;

    @ApiModelProperty(
            example="3",
            notes="Grade cannot be empty"
    )
    private int grade;

    @ApiModelProperty(
            example="30",
            notes="Number Lesson cannot be empty"
    )
    private int numLesson;


    @ApiModelProperty(
            example="300000",
            notes="Price cannot be empty"
    )
    private Long fee;

    @Size(max = 255)
    @ApiModelProperty(
            example="Note",
            notes="Note cannot be empty"
    )
    private String note;

    private List<LessonRequest> listLesson;

    @ApiModelProperty(
            example="2021-08-09",
            notes="Begin day cannot be empty"
    )
    private Date beginDay;
}
