package com.example.tenretni.core

import kotlinx.serialization.json.Json

abstract class JsonDataSource {
    private val _json = Json { ignoreUnknownKeys = true }

    protected val json get() = _json
}
