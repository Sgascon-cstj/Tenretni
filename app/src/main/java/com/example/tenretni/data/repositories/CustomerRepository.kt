package com.example.tenretni.data.repositories

import com.example.tenretni.core.ApiResult
import com.example.tenretni.data.datasources.CustomerDataSource
import com.example.tenretni.domain.models.Customer
import com.example.tenretni.domain.models.Gateway
import com.example.tenretni.domain.models.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CustomerRepository {

    private val customerDataSource = CustomerDataSource()

    fun retrieveGateways(href: String): Flow<ApiResult<List<Gateway>>>? {
        return flow {
            emit(ApiResult.Loading)
            try{
                emit(ApiResult.Success(customerDataSource.retrieveGateways(href)))
            } catch(ex: Exception) {
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }
}