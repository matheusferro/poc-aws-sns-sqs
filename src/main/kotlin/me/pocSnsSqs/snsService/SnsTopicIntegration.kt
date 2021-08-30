package me.pocSnsSqs.snsService

import me.pocSnsSqs.customer.CustomerModel
import me.pocSnsSqs.sqsService.EmailModel

interface SnsTopicIntegration {

    fun sendCustomerDataToTopic(customerModel: CustomerModel)

    fun sendEmail(emailModel: EmailModel)
}