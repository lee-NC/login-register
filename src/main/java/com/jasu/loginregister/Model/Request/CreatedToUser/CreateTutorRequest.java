package com.jasu.loginregister.Model.Request.CreatedToUser;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTutorRequest {


    @NotNull(message = "UserId is required")
    @ApiModelProperty(
            example= " 8",
            notes="UserId cannot be empty",
            required=true
    )
    private Long userId;

    @NotNull(message = "Literacy is required")
    @NotEmpty(message = "Literacy is required")
    @ApiModelProperty(
            example="Philosopher",
            notes="Literacy cannot be empty",
            required=true
    )
    private String literacy;


    private List<SchoolRequest> listTutorSchool;


    @ApiModelProperty(
            example="2.5",
            notes="Fill your experience years you have"
    )
    private float experience;


    private List<AchievementRequest> listTutorAchievement;
}
