package me.pocSnsSqs.sqsService

import io.micronaut.core.annotation.Introspected

@Introspected
data class EmailModel(
    val subject: String,
    val sendTo: String,
    val messageBody: String
)