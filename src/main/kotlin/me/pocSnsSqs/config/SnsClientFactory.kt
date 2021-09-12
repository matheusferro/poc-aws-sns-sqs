package me.pocSnsSqs.config

import io.micronaut.context.annotation.*
import software.amazon.awssdk.services.sns.SnsClient
import java.net.URI

@Factory
@Requires(env = ["dev", "prod"])
class SnsClientFactory(
    @Value("\${aws.configurations.url}") private val AWS_URL: String
    //,@Value("\${aws.configurations.region}") private val AWS_REGION: String
    //,@Value("\${aws.configurations.keyId}") private val AWS_KEY_ID: String,
    //@Value("\${aws.configurations.accessKey}") private val AWS_ACCESS_KEY: String
) {

    @Bean
    @Primary
    fun buildSnsClient(): SnsClient = SnsClient.builder()
        .endpointOverride(URI.create(AWS_URL))
        //Environment variables already set in docker-compose.yaml
        //.region(Region.of(AWS_REGION))
        //.credentialsProvider(
        //    StaticCredentialsProvider.create(AwsBasicCredentials.create(AWS_KEY_ID, AWS_ACCESS_KEY))
        //)
        //.overrideConfiguration()
        .build()
}