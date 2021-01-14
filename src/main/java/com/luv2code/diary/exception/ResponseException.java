package com.luv2code.diary.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ResponseException {

    private final String message;

    private final HttpStatus httpStatus;

    private final ZonedDateTime zonedDateTime;

    public ResponseException(final String message,
                             final HttpStatus httpStatus,
                             final ZonedDateTime zonedDateTime) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.zonedDateTime = zonedDateTime;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }
}
