/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.mapper;

import com.tailrocks.example.grpc.v1.payment.method.PaymentMethod;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodCard;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodCardInput;
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodInput;
import com.tailrocks.example.jooq.tables.records.PaymentMethodRecord;
import com.zhokhov.jambalaya.micronaut.mapstruct.protobuf.ProtobufConvertersMapper;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(
        componentModel = "jsr330",
        injectionStrategy = CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {
                ProtobufConvertersMapper.class
        }
)
public interface PaymentMethodMapper {

    // mapping
    @Mapping(target = "card", source = "paymentMethodRecord")
    PaymentMethod toPaymentMethod(PaymentMethodRecord paymentMethodRecord);

    // mapping
    @Mapping(target = "brand", source = "cardBrand")
    @Mapping(target = "number", source = "cardNumber")
    @Mapping(target = "expirationYear", source = "cardExpirationDate.year")
    @Mapping(target = "expirationMonth", source = "cardExpirationDate.monthValue")
    @Mapping(target = "cardHolderName", source = "cardHolderName")
    PaymentMethodCard toPaymentMethodCard(PaymentMethodRecord paymentMethodRecord);

    // ignore
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    // mapping
    @Mapping(target = "cardBrand", source = "card.brand")
    @Mapping(target = "cardExpirationDate", source = "card", qualifiedByName = "toCardExpirationDate")
    @Mapping(target = "cardNumber", source = "card.number")
    @Mapping(target = "cardHolderName", source = "card.cardHolderName")
    PaymentMethodRecord toPaymentMethodRecord(PaymentMethodInput paymentMethodInput,
                                              @MappingTarget PaymentMethodRecord paymentMethodRecord);

    @Named("toCardExpirationDate")
    static LocalDate toCardExpirationDate(PaymentMethodCardInput card) {
        return LocalDate.of(
                card.getExpirationYear().getValue(),
                card.getExpirationMonth().getValue(),
                1
        );
    }

}
