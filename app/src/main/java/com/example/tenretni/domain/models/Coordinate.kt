package com.example.tenretni.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Coordinate (val latitude:Float = 0.0f, val longitude:Float = 0.0f)