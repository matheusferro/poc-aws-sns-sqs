package me.pocSnsSqs.factory

import com.fasterxml.jackson.databind.ObjectMapper
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import me.pocSnsSqs.NotifierApiServiceGrpc
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI

@Factory
class Clients {

    @Bean
    fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): NotifierApiServiceGrpc.NotifierApiServiceBlockingStub =
        NotifierApiServiceGrpc.newBlockingStub(channel)

    @Bean
    fun sqsClient(): SqsClient = SqsClient.builder()
        .endpointOverride(URI.create("http://localhost:4566"))
        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
        .build()

    @Bean
    fun snsClient(): SnsClient = SnsClient.builder()
        .endpointOverride(URI.create("http://localhost:4566"))
        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
        .build()

}