package com.reopenai.kalista.grpc.common.metadata;

import com.reopenai.kalista.base.constants.SystemConstants;
import io.grpc.Context;
import io.grpc.Metadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Locale;
import java.util.function.Function;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

/**
 * Created by Allen Huang
 */
@Getter
@RequiredArgsConstructor
public enum GrpcContextKey {

    REFERRER_SERVICE(
            Metadata.Key.of("Referrer-Service", ASCII_STRING_MARSHALLER),
            Context.key("Referrer-Service"),
            "Referrer-Service",
            Object::toString,
            Function.identity()
    ),

    LOCALE(
            Metadata.Key.of("Locale", ASCII_STRING_MARSHALLER),
            Context.key("Locale"),
            Locale.class,
            data -> data instanceof Locale locale ? locale.toLanguageTag() : null,
            Locale::forLanguageTag
    ),


    REQUEST_ID(
            Metadata.Key.of("X-APP-REQUEST-ID", ASCII_STRING_MARSHALLER),
            Context.key("X-APP-REQUEST-ID"),
            SystemConstants.REQUEST_ID,
            Object::toString,
            Function.identity()
    ),
    ;

    private final Metadata.Key<String> metadataKey;

    private final Context.Key<String> contextKey;

    private final Object appKey;

    private final Function<Object, String> encode;

    private final Function<String, ?> decode;

}
