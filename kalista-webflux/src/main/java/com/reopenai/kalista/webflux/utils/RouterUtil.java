package com.reopenai.kalista.webflux.utils;

import cn.hutool.core.collection.CollUtil;
import com.reopenai.kalista.core.web.ApiInfo;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Allen Huang
 */
public final class RouterUtil {

    public static List<ApiInfo> getApiInfos(RequestMappingHandlerMapping mapping) {
        return mapping.getHandlerMethods().entrySet().stream()
                .map(entry -> {
                    RequestMappingInfo requestMapping = entry.getKey();
                    HandlerMethod handlerMethod = entry.getValue();
                    return parseApiInfo(requestMapping, handlerMethod);
                })
                .filter(CollUtil::isNotEmpty)
                .flatMap(Collection::stream)
                .toList();
    }

    public static List<ApiInfo> parseApiInfo(RequestMappingInfo requestMapping, HandlerMethod handlerMethod) {
        Set<RequestMethod> methods = requestMapping.getMethodsCondition().getMethods();
        if (CollUtil.isEmpty(methods)) {
            return Collections.emptyList();
        }
        Set<String> uris = parseUris(requestMapping);
        if (CollUtil.isEmpty(uris)) {
            return Collections.emptyList();
        }
        List<ApiInfo> apiInfos = new ArrayList<>(methods.size() + uris.size());
        for (RequestMethod method : methods) {
            for (String uri : uris) {
                apiInfos.add(new ApiInfo(uri, method, handlerMethod));
            }
        }
        return apiInfos;
    }

    public static Set<String> parseUris(RequestMappingInfo requestMapping) {
        return Optional.of(requestMapping.getPatternsCondition())
                .map(PatternsRequestCondition::getPatterns)
                .stream()
                .flatMap(Collection::stream)
                .map(PathPattern::getPatternString)
                .collect(Collectors.toSet());
    }

}
