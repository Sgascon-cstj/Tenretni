package com.example.tenretni.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Coordinate (val latitude:Double = 0.0, val longitude:Double = 0.0)