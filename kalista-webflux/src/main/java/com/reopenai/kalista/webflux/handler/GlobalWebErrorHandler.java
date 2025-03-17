package com.reopenai.kalista.webflux.handler;

import com.reopenai.kalista.base.ErrorCode;
import com.reopenai.kalista.core.lang.exception.BusinessException;
import com.reopenai.kalista.core.serialization.jackson.JsonUtil;
import com.reopenai.kalista.core.web.ApiResponse;
import jakarta.validation.ValidationException;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Created by Allen Huang
 */
public class GlobalWebErrorHandler implements WebExceptionHandler, Ordered {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof ValidationException err) {
            return writeWith(exchange, ErrorCode.Builtin.VALIDATION_FAILED, err.getMessage());
        } else if (ex instanceof BusinessException err) {
            return writeWith(exchange, err.getErrorCode(), err.getArgs());
        }
        return Mono.error(ex);
    }

    private Mono<Void> writeWith(ServerWebExchange exchange, ErrorCode errorCode, Object... args) {
        Locale locale = exchange.getLocaleContext().getLocale();
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.BAD_REQUEST);
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
