package com.example.tenretni.data.datasources

import com.example.tenretni.core.Constants
import com.example.tenretni.core.JsonDataSource
import com.example.tenretni.domain.models.Ticket
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.decodeFromString
import java.lang.reflect.Parameter

class TicketDataSource : JsonDataSource() {

    fun retrieveAll(): List<Ticket> {
        val (_,_, result) = Constants.BaseURL.TICKETS.httpGet().responseJson()

        return when (result) {
            is Result.Success -> {
                json.decodeFromString(result.value.content)
            }
            is Result.Failure -> {
                throw result.error.exception
            }
        }
    }

    fun retrieveOne(href: String): Ticket {
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

    fun actions(href: String,action:String): Ticket {
        val url = "${href}/actions?type=${action}"
        val (_,_, result) = url.httpPost().responseJson()

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