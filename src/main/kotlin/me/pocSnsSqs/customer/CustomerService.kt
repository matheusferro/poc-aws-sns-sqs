package me.pocSnsSqs.customer

import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import me.pocSnsSqs.snsService.SnsTopicIntegration
import javax.validation.Valid

@Singleton
@Validated
class CustomerService(
    private val snsTopic: SnsTopicIntegration
){
    fun sendCustomerData(@Valid customer: CustomerModel) = snsTopic.sendCustomerDataToTopic(customer)
}