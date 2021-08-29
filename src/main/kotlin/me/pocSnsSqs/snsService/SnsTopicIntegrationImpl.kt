package me.pocSnsSqs.snsService

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import me.pocSnsSqs.customer.CustomerModel
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.MessageAttributeValue
import software.amazon.awssdk.services.sns.model.PublishRequest

@Singleton
class SnsTopicIntegrationImpl(
    private val objMp: ObjectMapper,
    private val snsClient: SnsClient,
    @Value("\${aws.sns.subscription-topic.arn}") private val snsArn: String
) : SnsTopicIntegration {
    override fun sendCustomerDataToTopic(customerModel: CustomerModel) {
        val publicRequest = PublishRequest.builder()
            //Here we can define an FilterPolicy to
            // decide whether the subscribed endpoint will recive message
            .messageAttributes(
                mapOf(
                    Pair("notify_option",
                        MessageAttributeValue.builder()
                            .dataType("String")
                            .stringValue(customerModel.notifyOption)
                            .build()
                    )
                )
            )
            .targetArn(snsArn)
            .subject("Customer_data")
            //.messageStructure()
            //Configuration for FIFO, messages in the
            // same message group are delivered in order
            //.messageGroupId("")
            .message(objMp.writeValueAsString(customerModel))
            .build()
        snsClient.publish(publicRequest)
    }
}