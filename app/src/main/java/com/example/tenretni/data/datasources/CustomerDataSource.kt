package com.example.tenretni.data.datasources

import com.example.tenretni.core.Constants
import com.example.tenretni.core.JsonDataSource
import com.example.tenretni.domain.models.Customer
import com.example.tenretni.domain.models.Gateway
import com.example.tenretni.domain.models.Ticket
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.decodeFromString

class CustomerDataSource : JsonDataSource() {

    fun retrieveGateways(href: String): List<Gateway> {
        val (_,_, result) = Constants.BaseURL.CUSTOMERS_GATEWAYS.format(href).httpGet().responseJson()

        return when (result) {
            is Result.Success -> {
                json.decodeFromString(result.value.content)
            }
            is Result.Failure -> {
                throw result.error.exception
            }
        }
    }
    fun create(rawValue: String, href: String) : Gateway {

        val url = href + "/gateways"
        val (_, _, result) = url.httpPost().jsonBody(rawValue).responseJson()

        //Gérer la réponse
        return when(result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }

    }

}