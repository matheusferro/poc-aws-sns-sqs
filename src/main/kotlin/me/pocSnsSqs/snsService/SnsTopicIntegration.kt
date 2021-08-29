package me.pocSnsSqs.snsService

import me.pocSnsSqs.customer.CustomerModel

interface SnsTopicIntegration {

    fun sendCustomerDataToTopic(customerModel: CustomerModel)
}