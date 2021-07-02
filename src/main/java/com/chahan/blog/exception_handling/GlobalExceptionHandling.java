package com.chahan.blog.exception_handling;

import com.chahan.blog.exception.BaseApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandling {
    @ExceptionHandler
    public ResponseEntity<ApiError> handleException(
            BaseApiException exception) {
        ApiError data = new ApiError();
        data.setMessage(exception.getMessage());
        data.setStatus(exception.getStatus().value());
        return new ResponseEntity<>(data,exception.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleException(
            Exception exception) {
        ApiError data = new ApiError();
        data.setMessage(exception.getMessage());
        data.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

}
