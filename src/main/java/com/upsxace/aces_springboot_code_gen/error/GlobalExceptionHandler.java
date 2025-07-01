package com.upsxace.aces_springboot_code_gen.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        var message = "Bad request";

        if (ex.getMessage().startsWith("Required request body is missing"))
            message = "Required request body is missing";

        if(message.equals("Bad request"))
            logger.error("Bad request", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorsDto> handleValidationException(MethodArgumentNotValidException ex) {
        var errors = new ErrorsDto();
        ex.getBindingResult().getFieldErrors().forEach(e -> errors.addError(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> handleBadRequestExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleAllExceptions(Exception ex) {
        logger.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("Internal error"));
    }
}