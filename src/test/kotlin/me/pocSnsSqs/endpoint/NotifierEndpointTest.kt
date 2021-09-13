package me.pocSnsSqs.endpoint

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import me.pocSnsSqs.NotifierApiServiceGrpc
import me.pocSnsSqs.SendCustomerDataRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@MicronautTest
class NotifierEndpointTest(
    private val grpcClient: NotifierApiServiceGrpc.NotifierApiServiceBlockingStub
) {
    @Test
    fun `should be send customer data`() {
        with(
            grpcClient.sendCustomerData(
                SendCustomerDataRequest.newBuilder().apply {
                    this.name = "testcustomer"
                    this.email = "customer@gmail.com"
                    this.phone = "999999999"
                    this.notifyOption = SendCustomerDataRequest.NOTIFYOPT.SPORTS
                }.build()
            )
        ) {
            assertEquals("Subscription successful.", this.message)
        }
    }
}

