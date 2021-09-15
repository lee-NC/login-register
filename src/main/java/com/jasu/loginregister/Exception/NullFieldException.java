package com.jasu.loginregister.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NullFieldException extends NullPointerException {
    public NullFieldException(String message) {
        super(message);
    }
}
