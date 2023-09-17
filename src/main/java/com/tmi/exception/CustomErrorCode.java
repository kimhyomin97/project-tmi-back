package com.tmi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CustomErrorCode {
    // 400 Bad Request
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "fail", "Invalid Input Value"),
    // 401 Unauthorized
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "fail", "UnAuthorized"),
    // 403 Forbidden
    FORBIDDEN(HttpStatus.FORBIDDEN, "fail", "Forbidden"),
    // 404 Not Found
    NOT_FOUND(HttpStatus.NOT_FOUND, "fail", "Not Found"),
    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "fail", "Server Error"),
    // 503 Service Unavailable
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "fail", "Service Unavailable"),
    INVALID_LATLON(HttpStatus.BAD_REQUEST, "fail", "Invalid LatLon"), // 잘못된 좌표값 입력
    
    
    ;

    private final HttpStatus httpStatus;
    private final String result;
    private final String message;
}
