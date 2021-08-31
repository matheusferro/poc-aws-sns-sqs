package me.pocSnsSqs.sqsService

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.context.annotation.Value
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import me.pocSnsSqs.snsService.SnsTopicIntegration
import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest

@Singleton
class SqsIntegration(
    private val snsTopicIntegration: SnsTopicIntegration,
    private val sqsClient: SqsClient,
    private val objMapper: ObjectMapper,
    @Value("\${aws.sqs.notification-queue.url}") private val AWS_SQS_URL:  String
){

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelay = "10s")
    fun verifyNotificationsQueue(){
        val receiveMessageRequest = ReceiveMessageRequest.builder()
            .queueUrl(AWS_SQS_URL)
            .maxNumberOfMessages(10)
            .waitTimeSeconds(3)
            .build()
        val messagesResponse = sqsClient.receiveMessage(receiveMessageRequest)
        if(messagesResponse.hasMessages()){
            try {
                messagesResponse.messages().forEach{ message ->
                    val emailModel = objMapper.readValue(message.body(), EmailModel::class.java)
                    snsTopicIntegration.sendEmail(emailModel)
                    sqsClient.deleteMessage(
                        DeleteMessageRequest.builder()
                            .queueUrl(AWS_SQS_URL)
                            .receiptHandle(message.receiptHandle())
                            .build()
                    )
                }
            }catch (ex: Exception){
                LOGGER.error("error on send messages by email\n ${ex.message}")
            }
        }
    }
}