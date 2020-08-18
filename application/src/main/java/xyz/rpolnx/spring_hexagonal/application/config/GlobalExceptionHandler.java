package xyz.rpolnx.spring_hexagonal.application.config;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import xyz.rpolnx.spring_hexagonal.application.exceptions.BadRequestException;
import xyz.rpolnx.spring_hexagonal.application.exceptions.BusinessException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ExceptionWrapper handleException(BadRequestException ex) {
        return new ExceptionWrapper(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionWrapper handleException(BusinessException ex) {
        return new ExceptionWrapper(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ExceptionWrapper handleException(Exception ex) {
        return new ExceptionWrapper(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
