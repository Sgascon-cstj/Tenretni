package com.example.tenretni.data.datasources

import com.example.tenretni.core.JsonDataSource
import com.example.tenretni.domain.models.Customer
import com.example.tenretni.domain.models.Ticket
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.decodeFromString

class CustomerDataSource : JsonDataSource() {

    fun retrieveOne(href: String): Customer {
        val (_,_, result) = href.httpGet().responseJson()

        return when (result) {
            is Result.Success -> {
                json.decodeFromString(result.value.content)
            }
            is Result.Failure -> {
                throw result.error.exception
            }
        }
    }

}