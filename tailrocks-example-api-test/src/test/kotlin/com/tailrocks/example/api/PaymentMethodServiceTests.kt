/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api

import com.tailrocks.example.api.client.TailrocksExampleClient
import com.tailrocks.example.grpc.v1.payment.method.PaymentMethodCardBrand
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest(transactional = false)
class PaymentMethodServiceTests(
    private val tailrocksExampleClient: TailrocksExampleClient
) {

    @Nested
    inner class CreatePaymentMethod {

        // given: payment method details
        private val givenAccountId = Date().time
        private val givenCardBrand = PaymentMethodCardBrand.PAYMENT_METHOD_CARD_BRAND_VISA
        private val givenCardNumber = "1234567890"
        private val givenCvc = 1223
        private val givenExpirationYear = 2035
        private val givenExpirationMonth = 6
        private val givenCardHolderName = "Gary Zub"

        init {
            // when: create a payment method
            val response = tailrocksExampleClient.createPaymentMethodWithResponse(
                givenAccountId,
                givenCardBrand,
                givenCardNumber,
                givenCvc,
                givenExpirationYear,
                givenExpirationMonth,
                givenCardHolderName
            )

            // then: a new payment method created
            response.itemCount shouldBe 1
            response.getItem(0).also {
                it.id shouldBeGreaterThan 0
                it.accountId shouldBe givenAccountId
                it.card.brand shouldBe PaymentMethodCardBrand.PAYMENT_METHOD_CARD_BRAND_VISA
                it.card.number shouldBe givenCardNumber
                it.card.expirationYear shouldBe givenExpirationYear
                it.card.expirationMonth shouldBe givenExpirationMonth
                it.card.cardHolderName shouldBe givenCardHolderName
            }
        }

        @Test
        fun `can not find unknown card number`() {
            // when
            val card = tailrocksExampleClient.findByCardNumber(givenAccountId, "123000")

            // then: an empty optional will be returned
            card.isEmpty.shouldBeTrue()
        }

        @Test
        fun `can find just created card`() {
            // when
            val response = tailrocksExampleClient.findByCardNumberWithResponse(givenAccountId, givenCardNumber)

            // then: a one card will be returned
            response.itemCount shouldBe 1
            response.getItem(0).also {
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
