package com.bajaj.backend.exception;

import com.bajaj.backend.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String DEFAULT_EMAIL = "sudhanshu1515.be23@chitkarauniversity.edu.in";

    private String getOfficialEmail() {
        String envEmail = System.getenv("OFFICIAL_EMAIL");
        return (envEmail != null && !envEmail.isEmpty()) ? envEmail : DEFAULT_EMAIL;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
                .success(false)
                .officialEmail(getOfficialEmail())
                .data("route method " + e.getHttpMethod() + " not found for this request")
                .build());
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(
            org.springframework.http.converter.HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
                .success(false)
                .officialEmail(getOfficialEmail())
                .data("Invalid Request Format")
                .build());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                .success(false)
                .officialEmail(getOfficialEmail())
                .data("Internal Server Error: " + e.getMessage())
                .build());
    }
}
