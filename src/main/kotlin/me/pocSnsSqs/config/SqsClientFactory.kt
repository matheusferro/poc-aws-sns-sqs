package me.pocSnsSqs.config

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Value
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI

@Factory
class SqsClientFactory(
    @Value("\${aws.configurations.url}") private val AWS_URL: String
){

    @Bean
    @Primary
    fun buildSqsClient(): SqsClient = SqsClient.builder()
        .endpointOverride(URI.create(AWS_URL))
        .build()
}