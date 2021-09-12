package me.pocSnsSqs.endpoint

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.pocSnsSqs.NotifierApiServiceGrpc
import me.pocSnsSqs.SendCustomerDataRequest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.containers.DockerComposeContainer
import java.io.File

@MicronautTest
class NotifierEndpointTest(
    private val grpcClient: NotifierApiServiceGrpc.NotifierApiServiceBlockingStub
) {
    companion object {
        private val instance: KDockerComposeContainer by lazy { defineDockerCompose() }

        class KDockerComposeContainer(file: File) : DockerComposeContainer<KDockerComposeContainer>(file)

        private fun defineDockerCompose() = KDockerComposeContainer(File("src/test/resources/docker-compose-test.yaml"))

        @BeforeAll
        @JvmStatic
        internal fun setUp() {
            runBlocking {
                instance.start()
                //To initialize and run scripts TODO: change to the best way
                delay(15000)
            }
        }

        @AfterAll
        @JvmStatic
        internal fun finish() {
            instance.stop()
        }
    }

    @Test
    fun `should be send customer data`() {
        with(
            grpcClient.sendCustomerData(
                SendCustomerDataRequest.newBuilder().apply {
                    this.name = "test customer"
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

