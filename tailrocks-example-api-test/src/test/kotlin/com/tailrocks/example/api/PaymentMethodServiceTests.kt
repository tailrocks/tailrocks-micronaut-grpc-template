/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api

import com.tailrocks.example.api.client.TailrocksExampleClient
import com.tailrocks.example.api.test.junit.OpenTelemetryExtension
import com.tailrocks.example.api.test.junit.OpenTelemetryUtils.THEN
import com.tailrocks.example.api.test.junit.OpenTelemetryUtils.WHEN_
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodCardBrand
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(OpenTelemetryExtension::class)
@MicronautTest(transactional = false)
class PaymentMethodServiceTests(
    private val tailrocksExampleClient: TailrocksExampleClient
) {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class CreatePaymentMethod {

        // GIVEN: payment method details
        private val givenAccountId = Date().time
        private val givenCardBrand = PaymentMethodCardBrand.PAYMENT_METHOD_CARD_BRAND_VISA
        private val givenCardNumber = "1234567890"
        private val givenCvc = 1223
        private val givenExpirationYear = 2035
        private val givenExpirationMonth = 6
        private val givenCardHolderName = "Gary Zub"

        @BeforeEach
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
                    it.id shouldBeGreaterThan 0
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
                tailrocksExampleClient.findByCardNumber(givenAccountId, "123000")
            }

            THEN("an empty optional will be returned") {
                card.isEmpty.shouldBeTrue()
            }
        }

        @Test
        fun `can find just created card`() {
            val card = WHEN_ {
                tailrocksExampleClient.findByCardNumber(givenAccountId, givenCardNumber)
            }

            THEN("a one card will be returned") {
                card.isPresent.shouldBeTrue()
                card.get().also {
                    it.id shouldBeGreaterThan 0
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
