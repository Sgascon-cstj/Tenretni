package com.example.tenretni.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Gateway (
    val href:String,
    val serialNumber:String,
    val revision:String,
    val pin:String,
    val hash:String,
    val connection: Connection,
    val customer: Customer,
    val config: Config
)