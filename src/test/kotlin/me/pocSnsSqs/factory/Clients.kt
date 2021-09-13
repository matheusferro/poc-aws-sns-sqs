package me.pocSnsSqs.factory

import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import me.pocSnsSqs.NotifierApiServiceGrpc
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.PublishRequest
import software.amazon.awssdk.services.sns.model.PublishResponse
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse

@Factory
class Clients {

    @Bean
    fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): NotifierApiServiceGrpc.NotifierApiServiceBlockingStub =
        NotifierApiServiceGrpc.newBlockingStub(channel)

    @Bean
    fun sqsClient(): SqsClient = MockSqs()

    @Bean
    fun snsClient(): SnsClient = MockSns()
}

class MockSns() : SnsClient {
    override fun close() = println("close")

    override fun serviceName(): String = "sns"

    override fun publish(publishRequest: PublishRequest?): PublishResponse = PublishResponse.builder().build()
}

class MockSqs() : SqsClient {
    override fun close() = println("close")

    override fun serviceName(): String = "sqs"

    override fun receiveMessage(receiveMessageRequest: ReceiveMessageRequest?): ReceiveMessageResponse =
        ReceiveMessageResponse.builder().build()
}