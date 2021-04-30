/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.config;

import io.envoyproxy.pgv.ReflectiveValidatorIndex;
import io.envoyproxy.pgv.ValidatorIndex;
import io.envoyproxy.pgv.grpc.ValidatingServerInterceptor;
import io.grpc.ServerInterceptor;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;

@Factory
public class GrpcConfig {

    @Bean
    @Singleton
    public ServerInterceptor validationServerInterceptor() {
        // Create a validator index that reflectively loads generated validators
        ValidatorIndex index = new ReflectiveValidatorIndex();

        return new ValidatingServerInterceptor(index);
    }

}
