package com.reopenai.kalista.webflux.filter;

import cn.hutool.core.util.IdUtil;
import com.reopenai.infra.monitor.metric.StandardMetrics;
import com.reopenai.kalista.base.constants.SystemConstants;
import com.reopenai.kalista.core.bench.BenchMakerIgnore;
import com.reopenai.kalista.core.bench.BenchMarker;
import com.reopenai.kalista.core.web.HttpConstants;
import io.micrometer.core.instrument.Metrics;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Allen Huang
 */
@Slf4j
@Component
@BenchMakerIgnore
public class RequestContextFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethod().name();
        String path = request.getPath().toString();
        String queryParams = getQueryParams(request);
        BenchMarker benchMarker = new BenchMarker();
        HttpHeaders headers = request.getHeaders();

        Locale locale = resolveLocale(exchange);
        String requestId = resolveRequestId(headers);
        return chain.filter(exchange)
                .doOnSubscribe(s -> benchMarker.mark("enter"))
                .doFinally(signalType -> {
                    benchMarker.mark("outer");
                    HttpStatusCode statusCode = exchange.getResponse().getStatusCode();
                    log.info("httpRequest: [method={}]#[path={}]#[queryParams={}]#[status={}] {}", method, path, queryParams, statusCode, benchMarker.getResult());
                    if (statusCode != null && statusCode.is5xxServerError()) {
                        Metrics.counter(StandardMetrics.HTTP_REQUESTS_ERROR, "uri", path, "status", String.valueOf(statusCode.value())).increment();
                    }
                })
                .contextWrite(ctx -> ctx
                        .put(Locale.class, locale)
                        .put(BenchMarker.class, benchMarker)
                        .put(ServerWebExchange.class, exchange)
                        .put(SystemConstants.REQUEST_ID, requestId)
                )
                .contextCapture();
    }

    private String resolveRequestId(HttpHeaders headers) {
        String requestId = Optional.ofNullable(headers.getFirst(HttpConstants.HEADER_KEY_REQUEST_ID))
                .orElseGet(IdUtil::nanoId);
        MDC.put(SystemConstants.REQUEST_ID, requestId);
        return requestId;
    }

    private Locale resolveLocale(ServerWebExchange exchange) {
        Locale locale = Optional.of(exchange.getLocaleContext())
                .map(LocaleContext::getLocale)
                .orElse(Locale.getDefault());
        LocaleContextHolder.setLocale(locale);
        return locale;
    }

    private String getQueryParams(ServerHttpRequest request) {
        MultiValueMap<String, String> params = request.getQueryParams();
        if (!params.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            return builder.toString();
        }
        return "NONE";
    }

}
