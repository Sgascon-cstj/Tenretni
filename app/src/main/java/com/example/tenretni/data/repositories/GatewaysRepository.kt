package com.example.tenretni.data.repositories

import com.example.tenretni.core.ApiResult
import com.example.tenretni.core.Constants
import com.example.tenretni.data.datasources.GatewaysDataSource
import com.example.tenretni.domain.models.Gateway
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import java.lang.Exception

class GatewaysRepository {
    private val gatewaysDataSource = GatewaysDataSource()

    fun retriveAll(): Flow<ApiResult<List<Gateway>>>{
        return flow<ApiResult<List<Gateway>>> {
            while(true) {
                emit(ApiResult.Loading)
                try {

                    emit(ApiResult.Success(gatewaysDataSource.retrieveAll()))
                }catch (e: Exception){
                    emit(ApiResult.Error(e))
                }

                delay(Constants.RefreshDelay.GATEWAY_LIST)
            }

        }.flowOn(Dispatchers.IO)
    }

    fun retrieveOne(hrefGateway: String) : Flow<ApiResult<Gateway>>? {
        return flow {
            while(true) {

                emit(ApiResult.Loading)
                try {

                    emit(ApiResult.Success(gatewaysDataSource.retrieveOne(hrefGateway)))
                }catch (ex: Exception){
                    emit(ApiResult.Error(ex))
                }

                // TODO: A verif le delai
                delay(Constants.RefreshDelay.GATEWAY_LIST)
            }

        }.flowOn(Dispatchers.IO)
    }

    fun update(href: String) : Flow<ApiResult<Gateway>> {
        return flow {
            try {
                emit(ApiResult.Success(gatewaysDataSource.update(href)))
            } catch (ex: Exception) {
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }

     fun reboot(href: String) : Flow<ApiResult<Gateway>> {
        return flow {
            try {
                emit(ApiResult.Success(gatewaysDataSource.reboot(href)))
            } catch (ex: Exception) {
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }
}