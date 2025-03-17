package com.reopenai.kalista.grpc.server.test;

import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

/**
 * Created by Allen Huang
 */
@Service
public class EchoService extends EchoServiceGrpc.EchoServiceImplBase {

    @Override
    public void echo(EchoProto.EchoRequest request, StreamObserver<EchoProto.EchoReply> responseObserver) {
        EchoProto.EchoReply reply = EchoProto.EchoReply
                .newBuilder()
                .setPayload(request.getPayload())
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
