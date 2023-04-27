package com.example.tenretni.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Ticket (
    val customer: Customer,
    val href: String,
    val ticketNumber: String = "",
    val createdDate: String = "",
    val priority: String = "",
    val status: String = ""
)