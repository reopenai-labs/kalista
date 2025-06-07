package com.reopenai.kalista.core.web;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

/**
 * Created by Allen Huang
 */
public record ApiInfo(
        String uri,
        RequestMethod method,
        HandlerMethod handlerMethod
) {
}
