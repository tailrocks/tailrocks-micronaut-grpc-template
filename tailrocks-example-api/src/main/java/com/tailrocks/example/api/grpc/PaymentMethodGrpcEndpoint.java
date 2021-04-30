/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.grpc;

import com.tailrocks.example.api.mapper.PaymentMethodMapper;
import com.tailrocks.example.api.repository.PaymentMethodRepository;
import com.tailrocks.example.api.tenant.Tenant;
import com.tailrocks.example.grpc.v1.payment.method.CreatePaymentMethodRequest;
import com.tailrocks.example.grpc.v1.payment.method.FindPaymentMethodRequest;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethod;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodInput;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodListResponse;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodServiceGrpc;
import com.tailrocks.example.jooq.tables.records.PaymentMethodRecord;
import io.grpc.stub.StreamObserver;

import javax.inject.Singleton;
import java.util.List;

import static com.tailrocks.example.api.grpc.interceptor.TenantServerInterceptor.TENANT_ID;
import static java.util.stream.Collectors.toList;

@Singleton
public class PaymentMethodGrpcEndpoint extends PaymentMethodServiceGrpc.PaymentMethodServiceImplBase {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;

    public PaymentMethodGrpcEndpoint(
            PaymentMethodRepository paymentMethodRepository,
            PaymentMethodMapper paymentMethodMapper
    ) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodMapper = paymentMethodMapper;
    }

    @Override
    public void find(FindPaymentMethodRequest request,
                     StreamObserver<PaymentMethodListResponse> responseObserver) {
        List<PaymentMethod> items = paymentMethodRepository.find(TENANT_ID.get(), request).stream()
                .map(paymentMethodMapper::toPaymentMethod)
                .collect(toList());

        responseObserver.onNext(PaymentMethodListResponse.newBuilder()
                .addAllItem(items)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void create(CreatePaymentMethodRequest request,
                       StreamObserver<PaymentMethodListResponse> responseObserver) {
        List<PaymentMethod> items = request.getItemList().stream()
                .map(it -> findOrCreate(TENANT_ID.get(), it))
                .map(paymentMethodMapper::toPaymentMethod)
                .collect(toList());

        responseObserver.onNext(PaymentMethodListResponse.newBuilder()
                .addAllItem(items)
                .build());
        responseObserver.onCompleted();
    }

    public PaymentMethodRecord findOrCreate(Tenant tenant, PaymentMethodInput it) {
        return paymentMethodRepository.findByAccountIdAndCardNumber(
                tenant,
                it.getAccountId().getValue(),
                it.getCard().getNumber().getValue()
        ).orElseGet(() -> paymentMethodRepository.create(tenant, it));
    }

}
