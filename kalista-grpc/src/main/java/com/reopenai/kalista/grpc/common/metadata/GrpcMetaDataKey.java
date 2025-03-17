package com.reopenai.kalista.grpc.common.metadata;

import io.grpc.Metadata;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

/**
 * Created by Allen Huang
 */
public final class GrpcMetaDataKey {

    public static final Metadata.Key<String> ERROR_CODE = Metadata.Key.of("ErrorCode", ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<String> ERROR_MSG = Metadata.Key.of("ErrorMsg", ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<String> ERROR_PARAMS = Metadata.Key.of("ErrorParams", ASCII_STRING_MARSHALLER);

}
