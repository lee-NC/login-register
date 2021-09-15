package com.jasu.loginregister.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handlerNotFoundException(NotFoundException ex, WebRequest req) {
        // Log err

        ErrorResponse err = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateRecordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handlerDuplicateRecordException(DuplicateRecordException ex, WebRequest req) {
        // Log err

        ErrorResponse err = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullFieldException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handlerNullPointerException(NullFieldException ex, WebRequest req) {
        // Log err

        ErrorResponse err = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handlerForbiddenException(ForbiddenException ex, WebRequest req) {
        // Log err

        ErrorResponse err = new ErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handlerInternalServerException(InternalServerException ex, WebRequest req) {
        // Log err

        ErrorResponse err = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Xử lý tất cả các exception chưa được khai báo
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception ex, WebRequest req) {
        // Log err

        ErrorResponse err = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
