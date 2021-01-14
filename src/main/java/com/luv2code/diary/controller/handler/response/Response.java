package com.luv2code.diary.controller.handler.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@Builder
public class Response {

    private final ZonedDateTime zonedDateTime;

    private final Integer httpStatusCode;

    private final HttpStatus httpStatus;

    private final String message;

    private final String path;

    public Response(final ZonedDateTime zonedDateTime,
                    final Integer httpStatusCode,
                    final HttpStatus httpStatus,
                    final String message,
                    final String path) {
        this.zonedDateTime = zonedDateTime;
        this.httpStatusCode = httpStatusCode;
        this.message = message;
        this.httpStatus = httpStatus;
        this.path = path;
    }
}
