package com.example.tenretni.data.datasources

import com.example.tenretni.core.ApiResult
import com.example.tenretni.core.Constants
import com.example.tenretni.core.JsonDataSource
import com.example.tenretni.domain.models.Gateway
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.decodeFromString

class GatewaysDataSource : JsonDataSource() {
    fun retrieveAll() : List<Gateway>{
        val (_, _, result)  = Constants.BaseURL.GATEWAYS.httpGet().responseJson()

        return when(result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }

    fun retrieveOne(hrefGateway: String) : Gateway{
        val (_, _, result)  = hrefGateway.httpGet().responseJson()

        return when (result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }

    fun update(href: String):Gateway{
        return action("/actions?type=update",href)
    }

    fun reboot(href: String):Gateway{
        return action("/actions?type=reboot", href)
    }
    private fun action(action: String, href: String) : Gateway
    {
        val url = "${href}${action}"
        val (_, _, result) = url.httpPost().responseJson()

        //Gérer la réponse
        return when(result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }

    }
}