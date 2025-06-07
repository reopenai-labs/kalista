package com.reopenai.kalista.webflux.utils;

import com.reopenai.kalista.core.web.HttpConstants;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by Allen Huang
 */
public final class HttpRequestUtil {

    public static Mono<Void> handleWithRequestBody(ServerWebExchange exchange, WebFilterChain chain, BiConsumer<String, MonoSink<Void>> handler) {
        return handleWithRequestBody(exchange, chain, body -> Mono.create(sink -> handler.accept(body, sink)));
    }

    public static Mono<Void> handleWithRequestBody(ServerWebExchange exchange, WebFilterChain chain, Function<String, Mono<Void>> handler) {
        ServerHttpRequest request = exchange.getRequest();
        HttpMethod method = request.getMethod();
        String bodyPayload = exchange.getAttribute(HttpConstants.ATTR_KEY_REQUEST_BODY);
        if (bodyPayload == null && method != HttpMethod.OPTIONS && method != HttpMethod.GET) {
            return DataBufferUtils.join(request.getBody())
                    .flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        DataBufferUtils.release(dataBuffer);
                        String body = new String(bytes, StandardCharsets.UTF_8);
                        exchange.getAttributes().put(HttpConstants.ATTR_KEY_REQUEST_BODY, body);
                        ServerHttpRequestDecorator requestDecorator = new ServerHttpRequestDecorator(request) {
                            @Override
                            public @NonNull Flux<DataBuffer> getBody() {
                                return Flux.defer(() -> {
                                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                                    return Mono.just(buffer);
                                });
                            }
                        };
                        return handler.apply(body)
                                .then(chain.filter(exchange.mutate().request(requestDecorator).build()));
                    });
        }
        return handler.apply(bodyPayload)
                .then(chain.filter(exchange));
    }

}
