package com.tarasiuk.votehub.exception.handler;

import com.tarasiuk.votehub.exception.NotFoundException;
import com.tarasiuk.votehub.exception.ValidationProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(Exception ex) {
        return createResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ValidationProcessingException.class)
    public ResponseEntity<ErrorResponse> voteProcessingException(Exception ex) {
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> createResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new ErrorResponse(httpStatus.value(), message, Instant.now().toString()), httpStatus);
    }

    private record ErrorResponse(int status, String message, String timestamp) {
    }

}
