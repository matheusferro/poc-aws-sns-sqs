package me.pocSnsSqs.snsService

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import me.pocSnsSqs.customer.CustomerModel
import me.pocSnsSqs.sqsService.EmailModel
import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.MessageAttributeValue
import software.amazon.awssdk.services.sns.model.PublishRequest
import software.amazon.awssdk.services.sns.model.SubscribeRequest

@Singleton
class SnsTopicIntegrationImpl(
    private val objMp: ObjectMapper,
    private val snsClient: SnsClient,
    @Value("\${aws.sns.subscription-topic.arn}") private val snsArnSubscription: String,
    @Value("\${aws.sns.notify-topic.arn}") private val snsArnNotify: String
) : SnsTopicIntegration {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    override fun sendCustomerDataToTopic(customerModel: CustomerModel) {
        try {
            val publishRequest = PublishRequest.builder()
                //Here we can define an FilterPolicy to
                // decide whether the subscribed endpoint will recive message
                .messageAttributes(
                    mapOf(
                        Pair(
                            "notify_option",
                            MessageAttributeValue.builder()
                                .dataType("String")
                                .stringValue(customerModel.notifyOption.toString())
                                .build()
                        )
                    )
                )
                .targetArn(snsArnSubscription)
                .subject("customer_data")
                //.messageStructure()
                //Configuration for FIFO, messages in the
                // same message group are delivered in order
                //.messageGroupId("")
                .message(objMp.writeValueAsString(customerModel))
                .build()
            snsClient.publish(publishRequest)
        } catch (ex: Exception) {
            LOGGER.error("Unexpected error while send customer data\n ${ex.message}")
            throw ex
        }
    }

    override fun sendEmail(emailModel: EmailModel) {
        try {
            val subscribeRequest = SubscribeRequest.builder()
                .topicArn(snsArnNotify)
                .protocol("email")
                .endpoint(emailModel.sendTo)
                .build()
            snsClient.subscribe(subscribeRequest)
            val publishRequest = PublishRequest.builder()
                .targetArn(snsArnNotify)
                .subject(emailModel.subject)
                .message(emailModel.messageBody)
                .build()
            snsClient.publish(publishRequest)
        } catch (ex: Exception) {
            LOGGER.error("Unexpected error while send email\n ${ex.message}")
            throw ex
        }
    }
}