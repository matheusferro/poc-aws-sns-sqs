package me.pocSnsSqs.customer

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class CustomerModel(
    @field:NotBlank(message = "Name cannot be blank or null.")
    val name: String,
    @field:NotBlank(message = "Email cannot be blank or null.")
    val email: String,
    @field:NotBlank(message = "Phone cannot be blank or null.")
    val phone: String,
    @field:NotBlank(message = "Customer should chose one notification option.")
    val notifyOption: NOTIFICATION
)

enum class NOTIFICATION{
    TECH,
    SPORTS
}