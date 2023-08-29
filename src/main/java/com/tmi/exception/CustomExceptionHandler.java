package com.tmi.exception;


import com.tmi.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException e) {
        // log.error(e.getMessage(), e);
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .message(e.getErrorCode().getMessage())
                .result("fail")
                .build();
        return ResponseEntity.status(e.getErrorCode().getHttpStatus().value()).body(errorResponseDto);
    }
}
