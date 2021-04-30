/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.client;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

import javax.inject.Singleton;

// TODO move to jambalaya
@Singleton
public class TenantClientInterceptor implements ClientInterceptor {

    public static final CallOptions.Key<String> TENANT_OPTION = CallOptions.Key.create("x-tenant-id");

    private static final Metadata.Key<String> X_TENANT_ID_KEY =
            Metadata.Key.of("x-tenant-id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next
    ) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                String tenant = callOptions.getOption(TENANT_OPTION);

                if (tenant != null) {
                    headers.put(X_TENANT_ID_KEY, tenant);
                }

                super.start(responseListener, headers);
            }
        };
    }

}
