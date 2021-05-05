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
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodListResponse;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodServiceGrpc;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
// FIXME rename to match the name of microservice, for example: TailrocksPaymentClient
public class TailrocksExampleClient extends AbstractClient {

    private final PaymentMethodServiceGrpc.PaymentMethodServiceBlockingStub paymentMethodServiceBlockingStub;

    public TailrocksExampleClient(
            PaymentMethodServiceGrpc.PaymentMethodServiceBlockingStub paymentMethodServiceBlockingStub
    ) {
        this.paymentMethodServiceBlockingStub = paymentMethodServiceBlockingStub;
    }

    public Optional<PaymentMethod> findByCardNumber(long accountId, String cardNumber) {
        return findByCardNumber(null, accountId, cardNumber);
    }

    public Optional<PaymentMethod> findByCardNumber(String tenant, long accountId, String cardNumber) {
        return returnFirst(findByCardNumberWithResponse(tenant, accountId, cardNumber));
    }

    public PaymentMethodListResponse findByCardNumberWithResponse(long accountId, String cardNumber) {
        return findByCardNumberWithResponse(null, accountId, cardNumber);
    }

    public PaymentMethodListResponse findByCardNumberWithResponse(String tenant, long accountId, String cardNumber) {
        return paymentMethodServiceBlockingStub
                .withOption(TenantClientInterceptor.TENANT_OPTION, requireTenant(tenant))
                .find(
                        FindPaymentMethodRequest.newBuilder()
                                .addCriteria(FindPaymentMethodRequest.Criteria.newBuilder()
                                        .addAccountId(accountId)
                                        .addNumber(cardNumber)
                                        .build())
                                .build()
                );
    }

    public PaymentMethod createPaymentMethod(
            long tailrocksAccountId, PaymentMethodCardBrand cardBrand, String cardNumber, int cvc, int expirationYear,
            int expirationMonth, String cardHolderName
    ) {
        return createPaymentMethod(
                null, tailrocksAccountId, cardBrand, cardNumber, cvc, expirationYear, expirationMonth,
                cardHolderName
        );
    }

    public PaymentMethod createPaymentMethod(
            String tenant, long tailrocksAccountId, PaymentMethodCardBrand cardBrand, String cardNumber, int cvc,
            int expirationYear, int expirationMonth, String cardHolderName
    ) {
        return createPaymentMethodWithResponse(
                tenant,
                tailrocksAccountId,
                cardBrand,
                cardNumber,
                cvc,
                expirationYear,
                expirationMonth,
                cardHolderName
        ).getItem(0);
    }

    public PaymentMethodListResponse createPaymentMethodWithResponse(
            long tailrocksAccountId, PaymentMethodCardBrand cardBrand, String cardNumber, int cvc, int expirationYear,
            int expirationMonth, String cardHolderName
    ) {
        return createPaymentMethodWithResponse(
                null,
                tailrocksAccountId,
                cardBrand,
                cardNumber,
                cvc,
                expirationYear,
                expirationMonth,
                cardHolderName
        );
    }

    public PaymentMethodListResponse createPaymentMethodWithResponse(
            String tenant, long tailrocksAccountId, PaymentMethodCardBrand cardBrand, String cardNumber, int cvc,
            int expirationYear, int expirationMonth, String cardHolderName
    ) {
        return paymentMethodServiceBlockingStub
                .withOption(TenantClientInterceptor.TENANT_OPTION, requireTenant(tenant))
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
                );
    }

    private Optional<PaymentMethod> returnFirst(PaymentMethodListResponse response) {
        if (response.getItemCount() > 0) {
            return Optional.of(response.getItem(0));
        } else {
            return Optional.empty();
        }
    }

}
