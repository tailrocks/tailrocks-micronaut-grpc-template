/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api

import com.tailrocks.example.api.client.TailrocksExampleClient
import com.zhokhov.jambalaya.junit.opentelemetry.OpenTelemetry
import com.zhokhov.jambalaya.junit.opentelemetry.OpenTelemetryUtils.GIVEN
import com.zhokhov.jambalaya.junit.opentelemetry.OpenTelemetryUtils.THEN
import com.zhokhov.jambalaya.junit.opentelemetry.OpenTelemetryUtils.WHEN
import com.zhokhov.jambalaya.tenancy.TenancyUtils.runWithTenant
import io.grpc.StatusRuntimeException
import io.kotest.matchers.booleans.shouldBeTrue
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.System.currentTimeMillis

@MicronautTest(transactional = false)
@OpenTelemetry
class TenantIntegrationTests constructor(
    private val tailrocksExampleClient: TailrocksExampleClient
) {

    @Test
    fun `tenant provisioning`() {
        val tenantName = "test${currentTimeMillis()}"

        val accountId = ObjectId.get().toHexString()
        val cardNumber = "4000 0000 0000 0000"

        GIVEN("throws an error - tenant does not exist") {
            runWithTenant(tenantName) {
                assertThrows<StatusRuntimeException> {
                    tailrocksExampleClient.findPaymentMethodByCardNumber(accountId, cardNumber)
                }
            }
        }

        WHEN("provision a new tenant") {
            tailrocksExampleClient.provisionTenant(tenantName)
        }

        THEN("returns empty result") {
            runWithTenant(tenantName) {
                val result = tailrocksExampleClient.findPaymentMethodByCardNumber(accountId, cardNumber)

                result.isEmpty.shouldBeTrue()
            }
        }

        WHEN("drop just created tenant") {
            tailrocksExampleClient.dropTenant(tenantName)
        }

        THEN("throws an error - tenant deleted") {
            runWithTenant(tenantName) {
                assertThrows<StatusRuntimeException> {
                    tailrocksExampleClient.findPaymentMethodByCardNumber(accountId, cardNumber)
                }
            }
        }
    }

}
