package com.example.banking_app.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandlerException {

    @ExceptionHandler(
            ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails>
    handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ErrorDetails error =
                new ErrorDetails(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage(),
                        request.getRequestURI()
                );

        return new ResponseEntity<>(
                error,
                HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(
            InsufficientBalanceException.class)
    public ResponseEntity<ErrorDetails>
    handleInsufficientBalanceException(
            InsufficientBalanceException ex,
            HttpServletRequest request) {

        ErrorDetails error =
                new ErrorDetails(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        request.getRequestURI()
                );

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST);
    }

    // catches any exception not handled above
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails>
    handleGlobalException(
            Exception ex,
            HttpServletRequest request) {

        ErrorDetails error =
                new ErrorDetails(
                        LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ex.getMessage(),
                        request.getRequestURI()
                );

        return new ResponseEntity<>(
                error,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
