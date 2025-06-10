package com.reopenai.kalista.webflux.handler;

import com.reopenai.kalista.base.ErrorCode;
import com.reopenai.kalista.core.lang.exception.BusinessException;
import com.reopenai.kalista.core.lang.exception.SystemException;
import com.reopenai.kalista.core.serialization.jackson.JsonUtil;
import com.reopenai.kalista.core.web.ApiResponse;
import com.reopenai.kalista.core.web.WebRequestException;
import com.reopenai.kalista.webflux.utils.HttpRequestUtil;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static com.reopenai.kalista.base.ErrorCode.Builtin.VALIDATION_FAILED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Created by Allen Huang
 */
@Slf4j
public class GlobalWebErrorHandler implements WebExceptionHandler, Ordered {

    @Override
    public @NonNull Mono<Void> handle(@NonNull ServerWebExchange exchange, Throwable ex) {
        return switch (ex) {
            case ValidationException err -> writeWith(exchange, BAD_REQUEST, VALIDATION_FAILED, err.getMessage());
            case WebRequestException err -> writeWith(exchange, err.getHttpStatus(), err.getErrorCode(), err.getArgs());
            case BusinessException err -> writeWith(exchange, BAD_REQUEST, err.getErrorCode(), err.getArgs());
            case SystemException err -> writeWith(exchange, INTERNAL_SERVER_ERROR, err.getErrorCode(), err.getArgs());
            default -> {
                String logPrefix = exchange.getLogPrefix();
                StringBuilder builder = HttpRequestUtil.buildRequestLog(exchange.getRequest());
                log.error("{} - {}", logPrefix, builder, ex);
                yield writeWith(exchange, INTERNAL_SERVER_ERROR, ErrorCode.Builtin.SERVER_ERROR);
            }
        };
    }

    private Mono<Void> writeWith(ServerWebExchange exchange, HttpStatus status, ErrorCode errorCode, Object... args) {
        Locale locale = exchange.getLocaleContext().getLocale();
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        HttpHeaders headers = response.getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ApiResponse<Void> result = ApiResponse.failure(locale, errorCode, args);
        String payload = JsonUtil.toJSONString(result);
        DataBuffer buffer = response.bufferFactory().wrap(payload.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

}
