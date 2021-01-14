package com.luv2code.diary.handler;

import com.luv2code.diary.exception.EntityAlreadyExistException;
import com.luv2code.diary.exception.EntityNotFoundException;
import com.luv2code.diary.exception.ResponseException;
import com.luv2code.diary.exception.UserNotActiveException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleGlobalException(final Exception exception, final HttpServletRequest httpServletRequest) {
        final HttpStatus intervalServer = HttpStatus.INTERNAL_SERVER_ERROR;
        return getModelAndView(intervalServer, exception, httpServletRequest);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ModelAndView handleNotFoundException(final EntityNotFoundException exception, final HttpServletRequest httpServletRequest) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        return getModelAndView(notFound, exception, httpServletRequest);
    }

    @ExceptionHandler(value = EntityAlreadyExistException.class)
    public ModelAndView handleAlreadyExistsException(final EntityAlreadyExistException exception, final HttpServletRequest httpServletRequest) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return getModelAndView(badRequest, exception, httpServletRequest);
    }

    @ExceptionHandler(value = UserNotActiveException.class)
    public ModelAndView handleNotActiveException(final UserNotActiveException exception, final HttpServletRequest httpServletRequest) {
        final HttpStatus badRequest = HttpStatus.NOT_ACCEPTABLE;
        return getModelAndView(badRequest, exception, httpServletRequest);
    }

    private ModelAndView getModelAndView(final HttpStatus httpStatus, final Exception exception, final HttpServletRequest httpServletRequest) {
        final ResponseException responseException = ResponseException.builder()
                .zonedDateTime(ZonedDateTime.now(ZoneId.of("Z")))
                .httpStatusCode(httpStatus.value())
                .httpStatus(httpStatus)
                .message(exception.getMessage())
                .path(httpServletRequest.getRequestURI())
                .build();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("timestamp", responseException.getZonedDateTime());
        modelAndView.addObject("httpStatusCode", responseException.getHttpStatusCode());
        modelAndView.addObject("httpStatus", responseException.getHttpStatus());
        modelAndView.addObject("message", responseException.getMessage());
        modelAndView.addObject("path", responseException.getPath());
        return modelAndView;
    }
}
