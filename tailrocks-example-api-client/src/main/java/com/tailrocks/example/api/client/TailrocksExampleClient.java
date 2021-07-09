/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.client;

import com.google.protobuf.StringValue;
import com.google.protobuf.UInt32Value;
import com.tailrocks.example.grpc.v1.payment.method.CreatePaymentMethodRequest;
import com.tailrocks.example.grpc.v1.payment.method.FindPaymentMethodRequest;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethod;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodCardBrand;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodCardInput;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodInput;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodServiceGrpc;
import com.zhokhov.jambalaya.grpc.v1.tenant.DropTenantRequest;
import com.zhokhov.jambalaya.grpc.v1.tenant.ProvisionTenantRequest;
import com.zhokhov.jambalaya.grpc.v1.tenant.TenantServiceGrpc;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.annotation.NonNull;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

import static com.tailrocks.example.api.client.config.Constants.DEFAULT_TENANT;
import static com.tailrocks.example.api.client.config.Constants.TENANT_SERVICE_NAME;
import static com.zhokhov.jambalaya.tenancy.TenancyUtils.callWithTenant;
import static com.zhokhov.jambalaya.tenancy.TenancyUtils.getTenantStringOrElse;

@Singleton
// FIXME rename to match the name of microservice, for example: TailrocksPaymentClient
public class TailrocksExampleClient {

    private final TenantServiceGrpc.TenantServiceBlockingStub tenantServiceBlockingStub;
    private final PaymentMethodServiceGrpc.PaymentMethodServiceBlockingStub paymentMethodServiceBlockingStub;

    @Property(name = DEFAULT_TENANT) String defaultTenant;

    public TailrocksExampleClient(
            @Named(TENANT_SERVICE_NAME) TenantServiceGrpc.TenantServiceBlockingStub tenantServiceBlockingStub,
            PaymentMethodServiceGrpc.PaymentMethodServiceBlockingStub paymentMethodServiceBlockingStub
    ) {
        this.tenantServiceBlockingStub = tenantServiceBlockingStub;
        this.paymentMethodServiceBlockingStub = paymentMethodServiceBlockingStub;
    }

    public void provisionTenant(@NonNull String name) {
        tenantServiceBlockingStub.provision(ProvisionTenantRequest.newBuilder()
                .setName(StringValue.of(name))
                .build());
    }

    public void dropTenant(@NonNull String name) {
        tenantServiceBlockingStub.drop(DropTenantRequest.newBuilder()
                .setName(StringValue.of(name))
                .build());
    }

    public Optional<PaymentMethod> findPaymentMethodByCardNumber(@NonNull String accountId, @NonNull String cardNumber) {
        return callWithTenant(getTenantString(), () -> paymentMethodServiceBlockingStub
                .find(
                        FindPaymentMethodRequest.newBuilder()
                                .addCriteria(FindPaymentMethodRequest.Criteria.newBuilder()
                                        .addAccountId(accountId)
                                        .addNumber(cardNumber)
                                        .build())
                                .build()
                )
                .getItemList().stream().findFirst());
    }

    public PaymentMethod createPaymentMethod(
            @NonNull String tailrocksAccountId, @NonNull PaymentMethodCardBrand cardBrand, @NonNull String cardNumber,
            int cvc, int expirationYear, int expirationMonth, @NonNull String cardHolderName
    ) {
        return callWithTenant(getTenantString(), () -> paymentMethodServiceBlockingStub
                .create(
                        CreatePaymentMethodRequest.newBuilder()
                                .addItem(
                                        PaymentMethodInput.newBuilder()
                                                .setAccountId(StringValue.of(tailrocksAccountId))
                                                .setCard(
                                                        PaymentMethodCardInput.newBuilder()
                                                                .setBrand(cardBrand)
                                                                .setNumber(StringValue.of(cardNumber))
                                                                .setCvc(UInt32Value.of(cvc))
                                                                .setExpirationYear(UInt32Value.of(expirationYear))
                                                                .setExpirationMonth(UInt32Value.of(expirationMonth))
                                                                .setCardHolderName(StringValue.of(cardHolderName))
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .getItem(0));
    }

    private String getTenantString() {
        return getTenantStringOrElse(defaultTenant);
    }

}
