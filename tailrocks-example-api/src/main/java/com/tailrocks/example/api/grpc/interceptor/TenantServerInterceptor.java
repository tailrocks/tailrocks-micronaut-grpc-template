/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.grpc.interceptor;

import com.tailrocks.example.api.tenant.Tenant;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;

import javax.inject.Singleton;

// TODO move to jambalaya
@Singleton
public class TenantServerInterceptor implements ServerInterceptor {

    private static final Metadata.Key<String> X_TENANT_ID_KEY =
            Metadata.Key.of("x-tenant-id", Metadata.ASCII_STRING_MARSHALLER);

    public static final Context.Key<Tenant> TENANT_ID = Context.key("x-tenant-id");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> serverCall,
            Metadata metadata,
            ServerCallHandler<ReqT, RespT> serverCallHandler
    ) {
        String tenantId = metadata.get(X_TENANT_ID_KEY);
        if (tenantId == null) {
            serverCall.close(
                    Status.INVALID_ARGUMENT.withDescription("\"x-tenant-id\" header is required"),
                    new Metadata()
            );
            return new ServerCall.Listener<>() {
            };
        }

        Tenant tenant;

        try {
            tenant = Tenant.valueOf(tenantId.trim().toUpperCase());
        } catch (IllegalArgumentException ignored) {
            serverCall.close(
                    Status.INVALID_ARGUMENT.withDescription("Unknown value in \"x-tenant-id\""),
                    new Metadata()
            );
            return new ServerCall.Listener<>() {
            };
        }

        Context context = Context.current().withValue(TENANT_ID, tenant);
        return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler);
    }

}
