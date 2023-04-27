package com.example.tenretni.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Config (
    val mac: String = "",
    val SSID: String = "",
    val version: String= "",
    val kernel: List<String> = listOf(),
    val kernelRevision: Int = 0
)
