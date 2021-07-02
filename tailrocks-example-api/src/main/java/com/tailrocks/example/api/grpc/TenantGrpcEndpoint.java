/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.grpc;

import com.zhokhov.jambalaya.grpc.v1.tenant.DropTenantRequest;
import com.zhokhov.jambalaya.grpc.v1.tenant.DropTenantResponse;
import com.zhokhov.jambalaya.grpc.v1.tenant.ProvisionTenantRequest;
import com.zhokhov.jambalaya.grpc.v1.tenant.ProvisionTenantResponse;
import com.zhokhov.jambalaya.grpc.v1.tenant.TenantServiceGrpc;
import com.zhokhov.jambalaya.grpc.v1.tenant.TenantStatus;
import com.zhokhov.jambalaya.tenancy.flyway.TenantFlywayMigrator;
import io.grpc.stub.StreamObserver;
import io.micronaut.context.annotation.Property;
import org.flywaydb.core.api.FlywayException;

import javax.inject.Singleton;
import java.sql.SQLException;

@Singleton
public class TenantGrpcEndpoint extends TenantServiceGrpc.TenantServiceImplBase {

    private final TenantFlywayMigrator pgTenantFlywayMigrator;

    public TenantGrpcEndpoint(
            @Property(name = "datasources.default.url") String url,
            @Property(name = "datasources.default.username") String username,
            @Property(name = "datasources.default.password") String password
    ) {
        this.pgTenantFlywayMigrator = new TenantFlywayMigrator(url, username, password);
    }

    @Override
    public void provision(ProvisionTenantRequest request, StreamObserver<ProvisionTenantResponse> responseObserver) {
        try {
            pgTenantFlywayMigrator.migrateSchema(request.getName().getValue());

            responseObserver.onNext(
                    ProvisionTenantResponse.newBuilder()
                            .setStatus(TenantStatus.TENANT_STATUS_CREATED)
                            .build()
            );
            responseObserver.onCompleted();
        } catch (FlywayException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void drop(DropTenantRequest request, StreamObserver<DropTenantResponse> responseObserver) {
        try {
            pgTenantFlywayMigrator.dropSchema(request.getName().getValue());

            responseObserver.onNext(
                    DropTenantResponse.newBuilder()
                            .setStatus(TenantStatus.TENANT_STATUS_DROPPED)
                            .build()
            );
            responseObserver.onCompleted();
        } catch (SQLException e) {
            responseObserver.onError(e);
        }
    }

}
