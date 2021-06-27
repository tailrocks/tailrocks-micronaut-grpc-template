/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.client;

import com.google.protobuf.StringValue;
import com.google.protobuf.UInt32Value;
import com.google.protobuf.UInt64Value;
import com.tailrocks.example.grpc.v1.payment.method.CreatePaymentMethodRequest;
import com.tailrocks.example.grpc.v1.payment.method.FindPaymentMethodRequest;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethod;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodCardBrand;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodCardInput;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodInput;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodServiceGrpc;
import io.micronaut.context.annotation.Value;

import javax.inject.Singleton;
import java.util.Optional;

import static com.zhokhov.jambalaya.tenancy.TenancyUtils.callWithTenant;

@Singleton
// FIXME rename to match the name of microservice, for example: TailrocksPaymentClient
public class TailrocksExampleClient {

    private final PaymentMethodServiceGrpc.PaymentMethodServiceBlockingStub paymentMethodServiceBlockingStub;

    // FIXME replace with correct property
    @Value("${tailrocks.example.default-tenant:}")
    String defaultTenant;

    public TailrocksExampleClient(
            PaymentMethodServiceGrpc.PaymentMethodServiceBlockingStub paymentMethodServiceBlockingStub
    ) {
        this.paymentMethodServiceBlockingStub = paymentMethodServiceBlockingStub;
    }

    public Optional<PaymentMethod> findByCardNumber(long accountId, String cardNumber) {
        return callWithTenant(defaultTenant, () -> paymentMethodServiceBlockingStub
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
            long tailrocksAccountId, PaymentMethodCardBrand cardBrand, String cardNumber, int cvc,
            int expirationYear, int expirationMonth, String cardHolderName
    ) {
        return callWithTenant(defaultTenant, () -> paymentMethodServiceBlockingStub
                .create(
                        CreatePaymentMethodRequest.newBuilder()
                                .addItem(
                                        PaymentMethodInput.newBuilder()
                                                .setAccountId(UInt64Value.of(tailrocksAccountId))
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

}
