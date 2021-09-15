package com.jasu.loginregister.Exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponse {
    private HttpStatus status;
    private String message;

}
