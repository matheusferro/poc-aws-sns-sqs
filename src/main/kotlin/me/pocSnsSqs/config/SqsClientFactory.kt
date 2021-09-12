package me.pocSnsSqs.config

import io.micronaut.context.annotation.*
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI

@Factory
@Requires(env = ["dev", "prod"])
class SqsClientFactory(
    @Value("\${aws.configurations.url}") private val AWS_URL: String
){

    @Bean
    @Primary
    fun buildSqsClient(): SqsClient = SqsClient.builder()
        .endpointOverride(URI.create(AWS_URL))
        .build()
}