package com.reopenai.kalista.webflux.springdoc;

import io.swagger.v3.oas.models.OpenAPI;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * Created by Allen Huang
 */
public final class OpenAPIUtil {

    public static void setOnlySuccessResponse(OpenAPI openAPI) {
        Optional.ofNullable(openAPI.getPaths())
                .map(LinkedHashMap::values)
                .stream()
                .flatMap(Collection::stream)
                .forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                    operation.getResponses().entrySet().removeIf(entry -> !"200".equals(entry.getKey()));
                }));
    }

}
