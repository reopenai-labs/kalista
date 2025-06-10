package com.reopenai.kalista.webflux.utils;

import cn.hutool.core.util.StrUtil;
import com.reopenai.kalista.base.structure.lambda.XFunction;
import com.reopenai.kalista.core.cache.local.CacheConfig;
import com.reopenai.kalista.core.cache.local.LocalCache;
import com.reopenai.kalista.core.web.HttpConstants;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.lang.NonNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by Allen Huang
 */
public final class HttpRequestUtil {

    private static List<String> IP_HEADERS = List.of("X-FORWARDED-FOR", "X-REAL-IP", "PROXY-CLIENT-IP", "WL-PROXY-CLIENT-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR");

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private static Set<String> ALLOWLIST = Set.of("/v3/api-docs*", "/**/public/**", "/**/private/**", "/swagger*/**", "/actuator/**", "/v3/api-docs/*");

    private static final XFunction.R1<String, Boolean> ALLOWLIST_CACHE = LocalCache.create("webflux.builtin.allowlist", url -> {
        for (String allowUrl : ALLOWLIST) {
            if (ANT_PATH_MATCHER.match(allowUrl, url)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }, new CacheConfig<>(16, 256, Duration.ofHours(1)));

    public static boolean inAllowlist(String path) {
        return ALLOWLIST_CACHE.call(path);
    }

    public static void resetAllowlist(Set<String> allowlist) {
        ALLOWLIST = Set.copyOf(allowlist);
    }

    public static void addAllowlist(String allowUrl) {
        Set<String> allowlist = new HashSet<>(ALLOWLIST);
        allowlist.add(allowUrl);
        ALLOWLIST = Set.copyOf(allowlist);
    }

    public static String getRealIP(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        return getRealIP(request);
    }

    public static String getRealIP(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        for (String ipHeader : IP_HEADERS) {
            String value = headers.getFirst(ipHeader);
            if (StrUtil.isNotBlank(value) && !"unknown".equalsIgnoreCase(value)) {
                return value;
            }
        }
        return Optional.ofNullable(request.getRemoteAddress())
                .map(InetSocketAddress::getAddress)
                .map(InetAddress::getHostAddress)
                .orElse("unknown");
    }

    public static void resetIpHeaders(List<String> ipHeaders) {
        IP_HEADERS = List.copyOf(ipHeaders);
    }

    public static void addIpHeader(String ipHeader) {
        List<String> ipHeaders = new ArrayList<>(IP_HEADERS);
        ipHeaders.add(ipHeader);
        IP_HEADERS = List.copyOf(ipHeaders);
    }

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

    public static StringBuilder buildRequestLog(ServerHttpRequest request) {
        URI uri = request.getURI();
        StringBuilder builder = new StringBuilder();
        builder.append("httpRequest: [method=").append(request.getMethod().name());
        builder.append("]#[host=").append(uri.getHost());
        builder.append("]#[path=").append(uri.getPath());
        String rawQuery = uri.getRawQuery();
        if (rawQuery != null) {
            builder.append("]#[queryParams=").append(rawQuery);
        }
        return builder.append(']');
    }

}
