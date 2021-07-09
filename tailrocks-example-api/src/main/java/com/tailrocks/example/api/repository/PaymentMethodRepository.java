/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.repository;

import com.tailrocks.example.api.mapper.PaymentMethodMapper;
import com.tailrocks.example.grpc.v1.payment.method.FindPaymentMethodRequest;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodInput;
import com.tailrocks.example.jooq.tables.records.PaymentMethodRecord;
import com.zhokhov.jambalaya.tenancy.jooq.AbstractTenantRepository;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.transaction.annotation.ReadOnly;
import org.bson.types.ObjectId;
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
public class PaymentMethodRepository extends AbstractTenantRepository {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMethodRepository.class);

    private final PaymentMethodMapper paymentMethodMapper;

    public PaymentMethodRepository(
            @Property(name = "micronaut.application.name") String applicationName,
            DSLContext dslContext,
            PaymentMethodMapper paymentMethodMapper
    ) {
        super(applicationName, dslContext);
        this.paymentMethodMapper = paymentMethodMapper;
    }

    @ReadOnly
    public Optional<PaymentMethodRecord> findByAccountIdAndCardNumber(@NonNull String accountId,
                                                                      @NonNull String cardNumber) {
        checkNotBlank(accountId, "accountId");
        checkNotBlank(cardNumber, "cardNumber");

        return getDslContext()
                .selectFrom(PAYMENT_METHOD)
                .where(PAYMENT_METHOD.ACCOUNT_ID.eq(accountId))
                .and(PAYMENT_METHOD.CARD_NUMBER.eq(cardNumber))
                .fetchOptional();
    }

    @ReadOnly
    public List<PaymentMethodRecord> find(@NonNull FindPaymentMethodRequest request) {
        checkNotNull(request, "request");

        return getDslContext()
                .selectFrom(PAYMENT_METHOD)
                .where(generateFindCondition(request.getCriteriaList()))
                .fetch();
    }

    @Transactional
    public PaymentMethodRecord create(@NonNull PaymentMethodInput paymentMethodInput) {
        checkNotNull(paymentMethodInput, "paymentMethodInput");

        var item = paymentMethodMapper.toPaymentMethodRecord(
                paymentMethodInput,
                getDslContext().newRecord(PAYMENT_METHOD)
        );

        item.setId(ObjectId.get().toHexString());
        item.store();

        LOG.info("Created {}", item.getId());

        return item;
    }

    private Condition generateFindCondition(List<FindPaymentMethodRequest.Criteria> criteriaList) {
        var result = DSL.noCondition();

        for (FindPaymentMethodRequest.Criteria criteria : criteriaList) {
            result = result.or(generateCondition(criteria));
        }

        return result;
    }

    private Condition generateCondition(FindPaymentMethodRequest.Criteria criteria) {
        var result = noCondition();

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
