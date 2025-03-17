package com.reopenai.kalista.webflux.handler;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

/**
 * 健康检查端点
 *
 * @author Allen Huang
 */
@Controller("CustomHealthEndpoint")
public class HealthEndpoint {

    @GetMapping("/healthz/ready")
    @Operation(summary = "健康检查端点", hidden = true)
    public @ResponseBody Mono<String> health() {
        return Mono.just("ok");
    }

}
