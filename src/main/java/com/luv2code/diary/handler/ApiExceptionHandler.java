package com.luv2code.diary.handler;

import com.luv2code.diary.exception.EntityAlreadyExistException;
import com.luv2code.diary.exception.ResponseException;
import com.luv2code.diary.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ModelAndView handleNotFoundException(final EntityNotFoundException exception) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        return getModelAndView(notFound, exception.getMessage());
    }

    @ExceptionHandler(value = EntityAlreadyExistException.class)
    public ModelAndView handleAlreadyExistsException(final EntityAlreadyExistException exception) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return getModelAndView(badRequest, exception.getMessage());
    }

    private ModelAndView getModelAndView(HttpStatus httpStatus, String message) {
        final ResponseException responseException = new ResponseException(
                message,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", responseException.getMessage());
        modelAndView.addObject("httpStatus", responseException.getHttpStatus());
        modelAndView.addObject("timestamp", responseException.getZonedDateTime());
        return modelAndView;
    }
}
