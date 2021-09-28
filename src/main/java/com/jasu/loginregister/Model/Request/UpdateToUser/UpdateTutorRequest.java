package com.jasu.loginregister.Model.Request.UpdateToUser;

import com.jasu.loginregister.Model.Request.CreatedToUser.AchievementRequest;
import com.jasu.loginregister.Model.Request.CreatedToUser.SchoolRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.util.List;

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
