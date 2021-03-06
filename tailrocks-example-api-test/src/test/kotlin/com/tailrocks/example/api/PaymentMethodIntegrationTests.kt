/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api

import com.tailrocks.example.api.client.TailrocksExampleClient
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodCardBrand
import com.zhokhov.jambalaya.junit.opentelemetry.OpenTelemetry
import com.zhokhov.jambalaya.junit.opentelemetry.OpenTelemetryUtils.THEN
import com.zhokhov.jambalaya.junit.opentelemetry.OpenTelemetryUtils.WHEN_
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@MicronautTest(transactional = false)
@OpenTelemetry
class PaymentMethodIntegrationTests(
    private val tailrocksExampleClient: TailrocksExampleClient
) {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class CreatePaymentMethod {

        // GIVEN: payment method details
        private val givenAccountId = ObjectId.get().toHexString()
        private val givenCardBrand = PaymentMethodCardBrand.PAYMENT_METHOD_CARD_BRAND_VISA
        private val givenCardNumber = "1234567890"
        private val givenCvc = 1223
        private val givenExpirationYear = 2035
        private val givenExpirationMonth = 6
        private val givenCardHolderName = "Gary Zub"

        @BeforeAll
        fun init() {
            val paymentMethod = WHEN_("create a payment method") {
                tailrocksExampleClient.createPaymentMethod(
                    givenAccountId,
                    givenCardBrand,
                    givenCardNumber,
                    givenCvc,
                    givenExpirationYear,
                    givenExpirationMonth,
                    givenCardHolderName
                )
            }

            THEN("a new payment method created") {
                paymentMethod.also {
                    it.id.shouldNotBeNull()
                    it.accountId shouldBe givenAccountId
                    it.card.brand shouldBe PaymentMethodCardBrand.PAYMENT_METHOD_CARD_BRAND_VISA
                    it.card.number shouldBe givenCardNumber
                    it.card.expirationYear shouldBe givenExpirationYear
                    it.card.expirationMonth shouldBe givenExpirationMonth
                    it.card.cardHolderName shouldBe givenCardHolderName
                }
            }
        }

        @Test
        fun `can not find unknown card number`() {
            val card = WHEN_ {
                tailrocksExampleClient.findPaymentMethodByCardNumber(givenAccountId, "123000")
            }

            THEN("an empty optional will be returned") {
                card.isEmpty.shouldBeTrue()
            }
        }

        @Test
        fun `can find just created card`() {
            val card = WHEN_ {
                tailrocksExampleClient.findPaymentMethodByCardNumber(givenAccountId, givenCardNumber)
            }

            THEN("a one card will be returned") {
                card.isPresent.shouldBeTrue()
                card.get().also {
                    it.id.shouldNotBeNull()
                    it.accountId shouldBe givenAccountId
                    it.card.brand shouldBe PaymentMethodCardBrand.PAYMENT_METHOD_CARD_BRAND_VISA
                    it.card.number shouldBe givenCardNumber
                    it.card.expirationYear shouldBe givenExpirationYear
                    it.card.expirationMonth shouldBe givenExpirationMonth
                    it.card.cardHolderName shouldBe givenCardHolderName
                }
            }
        }

    }

}
