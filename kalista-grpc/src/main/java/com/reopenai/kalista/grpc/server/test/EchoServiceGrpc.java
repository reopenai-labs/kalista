package com.reopenai.kalista.grpc.server.test;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 *
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class EchoServiceGrpc {

    private EchoServiceGrpc() {
    }

    public static final String SERVICE_NAME = "system.v1.EchoService";

    // Static method descriptors that strictly reflect the proto.
    private static volatile io.grpc.MethodDescriptor<EchoProto.EchoRequest,
            EchoProto.EchoReply> getEchoMethod;

    @io.grpc.stub.annotations.RpcMethod(
            fullMethodName = SERVICE_NAME + '/' + "echo",
            requestType = EchoProto.EchoRequest.class,
            responseType = EchoProto.EchoReply.class,
            methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<EchoProto.EchoRequest,
            EchoProto.EchoReply> getEchoMethod() {
        io.grpc.MethodDescriptor<EchoProto.EchoRequest, EchoProto.EchoReply> getEchoMethod;
        if ((getEchoMethod = EchoServiceGrpc.getEchoMethod) == null) {
            synchronized (EchoServiceGrpc.class) {
                if ((getEchoMethod = EchoServiceGrpc.getEchoMethod) == null) {
                    EchoServiceGrpc.getEchoMethod = getEchoMethod =
                            io.grpc.MethodDescriptor.<EchoProto.EchoRequest, EchoProto.EchoReply>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                                    .setFullMethodName(generateFullMethodName(SERVICE_NAME, "echo"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            EchoProto.EchoRequest.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            EchoProto.EchoReply.getDefaultInstance()))
                                    .setSchemaDescriptor(new EchoServiceMethodDescriptorSupplier("echo"))
                                    .build();
                }
            }
        }
        return getEchoMethod;
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static EchoServiceStub newStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<EchoServiceStub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<EchoServiceStub>() {
                    @Override
                    public EchoServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new EchoServiceStub(channel, callOptions);
                    }
                };
        return EchoServiceStub.newStub(factory, channel);
    }

    /**
     * Creates a new blocking-style stub that supports all types of calls on the service
     */
    public static EchoServiceBlockingV2Stub newBlockingV2Stub(
            io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<EchoServiceBlockingV2Stub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<EchoServiceBlockingV2Stub>() {
                    @Override
                    public EchoServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new EchoServiceBlockingV2Stub(channel, callOptions);
                    }
                };
        return EchoServiceBlockingV2Stub.newStub(factory, channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static EchoServiceBlockingStub newBlockingStub(
            io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<EchoServiceBlockingStub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<EchoServiceBlockingStub>() {
                    @Override
                    public EchoServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new EchoServiceBlockingStub(channel, callOptions);
                    }
                };
        return EchoServiceBlockingStub.newStub(factory, channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static EchoServiceFutureStub newFutureStub(
            io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<EchoServiceFutureStub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<EchoServiceFutureStub>() {
                    @Override
                    public EchoServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new EchoServiceFutureStub(channel, callOptions);
                    }
                };
        return EchoServiceFutureStub.newStub(factory, channel);
    }

    /**
     *
     */
    public interface AsyncService {

        /**
         *
         */
        default void echo(EchoProto.EchoRequest request,
                          io.grpc.stub.StreamObserver<EchoProto.EchoReply> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEchoMethod(), responseObserver);
        }
    }

    /**
     * Base class for the server implementation of the service EchoService.
     */
    public static abstract class EchoServiceImplBase
            implements io.grpc.BindableService, AsyncService {

        @Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return EchoServiceGrpc.bindService(this);
        }
    }

    /**
     * A stub to allow clients to do asynchronous rpc calls to service EchoService.
     */
    public static final class EchoServiceStub
            extends io.grpc.stub.AbstractAsyncStub<EchoServiceStub> {
        private EchoServiceStub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected EchoServiceStub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new EchoServiceStub(channel, callOptions);
        }

        /**
         *
         */
        public void echo(EchoProto.EchoRequest request,
                         io.grpc.stub.StreamObserver<EchoProto.EchoReply> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(
                    getChannel().newCall(getEchoMethod(), getCallOptions()), request, responseObserver);
        }
    }

    /**
     * A stub to allow clients to do synchronous rpc calls to service EchoService.
     */
    public static final class EchoServiceBlockingV2Stub
            extends io.grpc.stub.AbstractBlockingStub<EchoServiceBlockingV2Stub> {
        private EchoServiceBlockingV2Stub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected EchoServiceBlockingV2Stub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new EchoServiceBlockingV2Stub(channel, callOptions);
        }

        /**
         *
         */
        public EchoProto.EchoReply echo(EchoProto.EchoRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(
                    getChannel(), getEchoMethod(), getCallOptions(), request);
        }
    }

    /**
     * A stub to allow clients to do limited synchronous rpc calls to service EchoService.
     */
    public static final class EchoServiceBlockingStub
            extends io.grpc.stub.AbstractBlockingStub<EchoServiceBlockingStub> {
        private EchoServiceBlockingStub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected EchoServiceBlockingStub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new EchoServiceBlockingStub(channel, callOptions);
        }

        /**
         *
         */
        public EchoProto.EchoReply echo(EchoProto.EchoRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(
                    getChannel(), getEchoMethod(), getCallOptions(), request);
        }
    }

    /**
     * A stub to allow clients to do ListenableFuture-style rpc calls to service EchoService.
     */
    public static final class EchoServiceFutureStub
            extends io.grpc.stub.AbstractFutureStub<EchoServiceFutureStub> {
        private EchoServiceFutureStub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected EchoServiceFutureStub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new EchoServiceFutureStub(channel, callOptions);
        }

        /**
         *
         */
        public com.google.common.util.concurrent.ListenableFuture<EchoProto.EchoReply> echo(
                EchoProto.EchoRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(
                    getChannel().newCall(getEchoMethod(), getCallOptions()), request);
        }
    }

    private static final int METHODID_ECHO = 0;

    private static final class MethodHandlers<Req, Resp> implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final AsyncService serviceImpl;
        private final int methodId;

        MethodHandlers(AsyncService serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_ECHO:
                    serviceImpl.echo((EchoProto.EchoRequest) request,
                            (io.grpc.stub.StreamObserver<EchoProto.EchoReply>) responseObserver);
                    break;
                default:
                    throw new AssertionError();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(
                io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new AssertionError();
            }
        }
    }

    public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
        return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                .addMethod(
                        getEchoMethod(),
                        io.grpc.stub.ServerCalls.asyncUnaryCall(
                                new MethodHandlers<
                                        EchoProto.EchoRequest,
                                        EchoProto.EchoReply>(
                                        service, METHODID_ECHO)))
                .build();
    }

    private static abstract class EchoServiceBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
        EchoServiceBaseDescriptorSupplier() {
        }

        @Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return EchoProto.getDescriptor();
        }

        @Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("EchoService");
        }
    }

    private static final class EchoServiceFileDescriptorSupplier
            extends EchoServiceBaseDescriptorSupplier {
        EchoServiceFileDescriptorSupplier() {
        }
    }

    private static final class EchoServiceMethodDescriptorSupplier
            extends EchoServiceBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
        private final String methodName;

        EchoServiceMethodDescriptorSupplier(String methodName) {
            this.methodName = methodName;
        }

        @Override
        public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
            return getServiceDescriptor().findMethodByName(methodName);
        }
    }

    private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        io.grpc.ServiceDescriptor result = serviceDescriptor;
        if (result == null) {
            synchronized (EchoServiceGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                            .setSchemaDescriptor(new EchoServiceFileDescriptorSupplier())
                            .addMethod(getEchoMethod())
                            .build();
                }
            }
        }
        return result;
    }
}
