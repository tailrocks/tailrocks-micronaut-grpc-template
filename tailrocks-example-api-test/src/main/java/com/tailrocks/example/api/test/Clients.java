/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.test;

import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodServiceGrpc;
import io.envoyproxy.pgv.ReflectiveValidatorIndex;
import io.envoyproxy.pgv.ValidatorIndex;
import io.envoyproxy.pgv.grpc.ValidatingClientInterceptor;
import io.grpc.ManagedChannel;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.grpc.annotation.GrpcChannel;
import io.micronaut.grpc.server.GrpcServerChannel;

@Factory
public class Clients {

    // Create a validator index that reflectively loads generated validators
    private final ValidatorIndex index = new ReflectiveValidatorIndex();

    @Bean
    public PaymentMethodServiceGrpc.PaymentMethodServiceBlockingStub paymentMethodServiceBlockingStub(
            @GrpcChannel(GrpcServerChannel.NAME) ManagedChannel channel
    ) {
        return PaymentMethodServiceGrpc
                .newBlockingStub(channel)
                .withInterceptors(new ValidatingClientInterceptor(index));
    }

}
