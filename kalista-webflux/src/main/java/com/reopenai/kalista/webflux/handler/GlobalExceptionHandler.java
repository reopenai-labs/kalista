package com.reopenai.kalista.webflux.handler;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.reopenai.kalista.base.ErrorCode;
import com.reopenai.kalista.core.lang.exception.BusinessException;
import com.reopenai.kalista.core.lang.exception.SystemException;
import com.reopenai.kalista.core.web.ApiResponse;
import com.reopenai.kalista.core.web.WebRequestException;
import com.reopenai.kalista.webflux.utils.HttpRequestUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MimeType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.*;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Created by Allen Huang
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<Void> methodArgumentTypeMismatchException(ServerWebExchange exchange, MethodArgumentTypeMismatchException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        return ApiResponse.failure(locale, ErrorCode.Builtin.INVALID_PARAMETER, String.format("%s=%s", e.getName(), e.getValue()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ApiResponse<Void> validationException(ValidationException e) {
        return ApiResponse.failureWithMessage(ErrorCode.Builtin.VALIDATION_FAILED, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<Void> constraintViolationException(ConstraintViolationException e) {
        StringJoiner joiner = new StringJoiner(";");
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            joiner.add(constraintViolation.getMessage());
        }
        return ApiResponse.failureWithMessage(ErrorCode.Builtin.VALIDATION_FAILED, joiner.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> methodArgumentNotValidException(MethodArgumentNotValidException error) {
        BindingResult result = error.getBindingResult();
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            result.getAllErrors().forEach(err -> sb.append(err.getDefaultMessage()).append(";"));
            return ApiResponse.failureWithMessage(ErrorCode.Builtin.VALIDATION_FAILED, sb.toString());
        }
        return ApiResponse.failureWithMessage(ErrorCode.Builtin.VALIDATION_FAILED, error.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResponse<Void> onNoResourceFoundException(ServerWebExchange exchange, ServerHttpRequest request) {
        return ApiResponse.failureWithMessage(ErrorCode.Builtin.NOT_FOUND, String.format("%s not found", request.getPath()));
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(MethodNotAllowedException.class)
    public ApiResponse<Void> methodNotAllowedException(ServerWebExchange exchange, MethodNotAllowedException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        return ApiResponse.failure(locale, ErrorCode.Builtin.METHOD_NOT_ALLOWED, e.getHttpMethod());
    }


    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(UnsupportedMediaTypeStatusException.class)
    public ApiResponse<Void> unsupportedMediaTypeStatusException(ServerWebExchange exchange, UnsupportedMediaTypeStatusException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        String contentType = Optional.ofNullable(e.getContentType())
                .map(MimeType::toString)
                .orElse("");
        return ApiResponse.failure(locale, ErrorCode.Builtin.MEDIA_TYPE_NOT_ALLOWED, contentType);
    }

    // 无法接收
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(NotAcceptableStatusException.class)
    public ApiResponse<Void> httpMediaTypeNotAcceptableException(ServerWebExchange exchange, NotAcceptableStatusException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        return ApiResponse.failure(locale, ErrorCode.Builtin.NOT_ACCEPTABLE, " ");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestValueException.class)
    public ApiResponse<Void> missingRequestValueException(ServerWebExchange exchange, MissingRequestValueException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        Object[] args = new Object[]{e.getName(), e.getType().getSimpleName()};
        return ApiResponse.failure(locale, ErrorCode.Builtin.MISSING_REQUEST_PARAMETER, args);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServerWebInputException.class)
    public ApiResponse<Void> badRequest(ServerWebExchange exchange, ServerWebInputException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        Throwable cause = e.getCause();
        if (cause instanceof TypeMismatchException err) {
            String propertyName = err.getPropertyName();
            String values = Optional.ofNullable(err.getValue()).map(Object::toString).orElse("");
            String typeName = Optional.ofNullable(err.getRequiredType()).map(Class::getSimpleName).orElse("");
            return ApiResponse.failure(locale, ErrorCode.Builtin.PARAM_TYPE_MISMATCH, propertyName, values, typeName);
        }
        return ApiResponse.failure(locale, ErrorCode.Builtin.MISSING_REQUEST_PARAMETER);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<Void> httpMessageNotReadableException(ServerWebExchange exchange, HttpMessageNotReadableException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException err) {
            for (int i = 0; i < err.getPath().size(); i++) {
                JsonMappingException.Reference reference = err.getPath().get(i);
                String fieldName = reference.getFieldName();
                if (StrUtil.isNotBlank(fieldName)) {
                    String value = Optional.ofNullable(err.getValue())
                            .map(Object::toString)
                            .orElse("");
                    return ApiResponse.failure(locale, ErrorCode.Builtin.INVALID_PARAMETER, String.format("%s:%s", fieldName, value));
                }
            }
        } else if (cause instanceof JsonMappingException err) {
            StringJoiner builder = new StringJoiner(".");
            for (JsonMappingException.Reference reference : err.getPath()) {
                builder.add(reference.getFieldName());
            }
            String fieldName = builder.toString();
            if (StrUtil.isNotBlank(fieldName)) {
                return ApiResponse.failure(locale, ErrorCode.Builtin.INVALID_PARAMETER, fieldName);
            }
        } else if (cause instanceof ValidationException err) {
            return validationException(err);
        }
        return ApiResponse.failure(locale, ErrorCode.Builtin.INVALID_PARAMETER, "UNKNOWN");
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ApiResponse<Object>> systemExceptionHandler(ServerWebExchange exchange, SystemException error) {
        Locale locale = exchange.getLocaleContext().getLocale();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorCode errorCode = error.getErrorCode();
        ApiResponse<Object> result = ApiResponse.failure(locale, errorCode, error.getArgs());
        if (errorCode.getValue().equals(result.getMessage())) {
            result.setMessage(error.getMessage());
        }
        if (error instanceof WebRequestException err) {
            status = err.getHttpStatus();
        } else if (error instanceof BusinessException) {
            status = HttpStatus.BAD_REQUEST;
        }
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> ioExceptionHandler(ServerWebExchange exchange, IOException e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        log.error("[Exception]I/O Exception", e);
        return ApiResponse.failure(locale, ErrorCode.Builtin.SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> exceptionHandler(ServerWebExchange exchange, Exception e) {
        Locale locale = exchange.getLocaleContext().getLocale();
        String logPrefix = exchange.getLogPrefix();
        StringBuilder builder = HttpRequestUtil.buildRequestLog(exchange.getRequest());
        builder.append("#[logPrefix=").append(logPrefix).append("] ");
        log.error("{}", builder, e);
        return ApiResponse.failure(locale, ErrorCode.Builtin.SERVER_ERROR);
    }

}
