package io.rochekata.ip.adapter.web;

import io.rochekata.ip.domain.service.NorthCountriesServiceValidationException;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebInputException;

@RestController
@ControllerAdvice
public class ExceptionHandlingAdvice {

    @ExceptionHandler({NorthCountriesServiceValidationException.class, ServerWebInputException.class, HttpMessageConversionException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorView handleValidationError(Exception e) {
        return ErrorView.builder().message(e.getMessage()).exception(e.getClass().getCanonicalName()).build();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorView handle(Exception e) {
        return ErrorView.builder().message(e.getMessage()).exception(e.getClass().getCanonicalName()).build();
    }

    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class ErrorView {
        private final String exception;
        private final String message;
    }
}