package com.jasu.loginregister.Exception;

import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({ NullPointerException.class, SyntaxException.class })  // Co the bat nhieu loai exception
    public ResponseEntity<String> handleExceptionA(Exception e) {
        return ResponseEntity.status(432).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnwantedException(Exception e) {
        log.info(e.getMessage());
        return ResponseEntity.status(500).body("Unknown error");
    }
}
