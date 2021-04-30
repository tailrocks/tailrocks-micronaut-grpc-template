/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.repository;

import com.tailrocks.example.api.mapper.PaymentMethodMapper;
import com.tailrocks.example.api.tenant.Tenant;
import com.tailrocks.example.grpc.v1.payment.method.FindPaymentMethodRequest;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodInput;
import com.tailrocks.example.jooq.tables.records.PaymentMethodRecord;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.transaction.annotation.ReadOnly;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.tailrocks.example.jooq.tables.PaymentMethod.PAYMENT_METHOD;
import static com.zhokhov.jambalaya.checks.Preconditions.checkNotBlank;
import static com.zhokhov.jambalaya.checks.Preconditions.checkNotNull;
import static org.jooq.impl.DSL.noCondition;

@Singleton
public class PaymentMethodRepository extends AbstractRepository {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMethodRepository.class);

    private final PaymentMethodMapper paymentMethodMapper;

    public PaymentMethodRepository(
            DSLContext dslContext,
            PaymentMethodMapper paymentMethodMapper
    ) {
        super(dslContext);
        this.paymentMethodMapper = paymentMethodMapper;
    }

    @ReadOnly
    public Optional<PaymentMethodRecord> findByAccountIdAndCardNumber(@NonNull Tenant tenant, long accountId,
                                                                      @NonNull String cardNumber) {
        checkNotNull(tenant, "tenant");
        checkNotBlank(cardNumber, "cardNumber");

        return getDslContext(tenant)
                .selectFrom(PAYMENT_METHOD)
                .where(PAYMENT_METHOD.ACCOUNT_ID.eq(accountId))
                .and(PAYMENT_METHOD.CARD_NUMBER.eq(cardNumber))
                .fetchOptional();
    }

    @ReadOnly
    public List<PaymentMethodRecord> find(@NonNull Tenant tenant, @NonNull FindPaymentMethodRequest request) {
        checkNotNull(tenant, "tenant");
        checkNotNull(request, "request");

        return getDslContext(tenant)
                .selectFrom(PAYMENT_METHOD)
                .where(generateFindCondition(request.getCriteriaList()))
                .fetch();
    }

    @Transactional
    public PaymentMethodRecord create(
            @NonNull Tenant tenant,
            @NonNull PaymentMethodInput paymentMethodInput
    ) {
        checkNotNull(tenant, "tenant");
        checkNotNull(paymentMethodInput, "paymentMethodInput");

        PaymentMethodRecord item = paymentMethodMapper.toPaymentMethodRecord(
                paymentMethodInput,
                getDslContext(tenant).newRecord(PAYMENT_METHOD)
        );

        item.store();

        LOG.info("Created {}", item.getId());

        return item;
    }

    private Condition generateFindCondition(List<FindPaymentMethodRequest.Criteria> criteriaList) {
        Condition result = DSL.noCondition();

        for (FindPaymentMethodRequest.Criteria criteria : criteriaList) {
            result = result.or(generateCondition(criteria));
        }

        return result;
    }

    private Condition generateCondition(FindPaymentMethodRequest.Criteria criteria) {
        Condition result = noCondition();

        if (criteria.getAccountIdCount() > 0) {
            result = result.and(PAYMENT_METHOD.ACCOUNT_ID.in(criteria.getAccountIdList()));
        }

        if (criteria.getNumberCount() > 0) {
            result = result.and(PAYMENT_METHOD.CARD_NUMBER.in(criteria.getNumberList()));
        }

        if (result.toString().equals(noCondition().toString())) {
            throw new RuntimeException("No any criteria added to the condition");
        }

        return result;
    }

}
