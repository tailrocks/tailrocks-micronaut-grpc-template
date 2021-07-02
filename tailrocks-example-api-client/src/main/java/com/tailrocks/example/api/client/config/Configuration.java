/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.client.config;

import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodServiceGrpc;
import com.zhokhov.jambalaya.grpc.v1.tenant.TenantServiceGrpc;
import io.envoyproxy.pgv.ReflectiveValidatorIndex;
import io.envoyproxy.pgv.ValidatorIndex;
import io.envoyproxy.pgv.grpc.ValidatingClientInterceptor;
import io.grpc.ManagedChannel;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.grpc.annotation.GrpcChannel;

import javax.inject.Named;
import javax.inject.Singleton;

import static com.tailrocks.example.api.client.config.Constants.GRPC_CHANNEL;
import static com.tailrocks.example.api.client.config.Constants.TENANT_SERVICE_NAME;

@Factory
public class Configuration {

    private final ValidatorIndex index = new ReflectiveValidatorIndex();

    @Bean
    public PaymentMethodServiceGrpc.PaymentMethodServiceBlockingStub paymentMethodServiceBlockingStub(
            @GrpcChannel(GRPC_CHANNEL) ManagedChannel channel
    ) {
        return PaymentMethodServiceGrpc
                .newBlockingStub(channel)
                .withInterceptors(new ValidatingClientInterceptor(index));
    }

    @Singleton
    @Named(TENANT_SERVICE_NAME)
    public TenantServiceGrpc.TenantServiceBlockingStub tenantServiceBlockingStub(
            @GrpcChannel(GRPC_CHANNEL) ManagedChannel channel
    ) {
        return TenantServiceGrpc
                .newBlockingStub(channel)
                .withInterceptors(new ValidatingClientInterceptor(index));
    }

}
