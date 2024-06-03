package com.siperes.siperes.handler;

import com.siperes.siperes.dto.response.base.APIResponse;
import com.siperes.siperes.dto.response.base.ApiErrorResponse;
import com.siperes.siperes.dto.response.base.ErrorDTO;
import com.siperes.siperes.exception.*;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse handlerNoHandlerFoundException(NoHandlerFoundException e) {
        return new APIResponse(
                HttpStatus.BAD_REQUEST,
                "Url not found: " + e.getMessage()
        );
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponse handlerDataNotFoundException(DataNotFoundException e) {
        return new APIResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
    }

    @ExceptionHandler(UserNotActiveException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public APIResponse handlerUserNotActiveException(UserNotActiveException e) {
        return new APIResponse(
                HttpStatus.FORBIDDEN,
                e.getMessage()
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public APIResponse handlerForbiddenException(ForbiddenException e) {
        return new APIResponse(
                HttpStatus.FORBIDDEN,
                e.getMessage()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public APIResponse handlerAccessDeniedException(AccessDeniedException e) {
        return new APIResponse(
                HttpStatus.FORBIDDEN,
                e.getMessage()
        );
    }

    @ExceptionHandler(NotVerifiedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public APIResponse handlerNotVerifiedException(NotVerifiedException e) {
        return new APIResponse(
                HttpStatus.FORBIDDEN,
                e.getMessage()
        );
    }

    @ExceptionHandler(ServiceBusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponse handlerServiceBusinessException(ServiceBusinessException e) {
        return new APIResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse handlerIllegalArgumentException(IllegalArgumentException e) {
        return new APIResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public APIResponse handlerBadCredentialsException(BadCredentialsException e) {
        return new APIResponse(
                HttpStatus.UNAUTHORIZED,
                e.getMessage()
        );
    }

    @ExceptionHandler(MissingTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public APIResponse handlerMissingTokenException(MissingTokenException e) {
        return new APIResponse(
                HttpStatus.UNAUTHORIZED,
                e.getMessage()
        );
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public APIResponse handlerJwtException(JwtException e) {
        return new APIResponse(
                HttpStatus.UNAUTHORIZED,
                e.getMessage()
        );
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse handledDataValidationException(ValidationException exception) {
        return new APIResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse handlerConversionFailedException(ConversionFailedException exception) {
        return new APIResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        return new APIResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse handleConstraintViolationException (ConstraintViolationException exception) {
        return new APIResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    @ExceptionHandler(DataConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public APIResponse handledDataConflictException(DataConflictException exception) {
        return new APIResponse(
                HttpStatus.CONFLICT,
                exception.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Schema(name = "ApiErrorResponse", description = "Api error response body")
    public ApiErrorResponse handleMethodArgumentException(MethodArgumentNotValidException exception) {
        List<ErrorDTO> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> {
                    ErrorDTO errorDTO = new ErrorDTO(error.getField(), error.getDefaultMessage());
                    errors.add(errorDTO);
                });
        return new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation error",
                errors
        );
    }
}
