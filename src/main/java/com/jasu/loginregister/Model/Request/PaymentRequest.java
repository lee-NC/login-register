package com.jasu.loginregister.Model.Request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PaymentRequest {

    @NotNull(message = "fee is required")
    @ApiModelProperty(
            example="33333",
            notes="fee cannot be empty",
            required = true
    )
    private long fee;

    @NotNull(message = "currency is required")
    @ApiModelProperty(
            example="VND",
            notes="currency cannot be empty",
            required = true
    )
    private String currency;

    @NotNull(message = "method is required")
    @ApiModelProperty(
            example="charge",
            notes="method cannot be empty",
            required = true
    )
    private String method;

    @NotNull(message = "intent is required")
    @ApiModelProperty(
            example="pay for apply fee",
            notes="intent cannot be empty",
            required = true
    )
    private String intent;

//    @NotNull(message = "description is required")
//    @ApiModelProperty(
//            example="3",
//            notes="description cannot be empty",
//            required = true
//
//    )
//    private String description;

}
