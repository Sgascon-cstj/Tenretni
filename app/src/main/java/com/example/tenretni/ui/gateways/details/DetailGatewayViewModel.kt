package com.example.tenretni.ui.gateways.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tenretni.core.ApiResult
import com.example.tenretni.data.repositories.GatewaysRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailGatewayViewModel(private val hrefGateway:String) : ViewModel() {

    private val gatewayRepository = GatewaysRepository()

    private val _detailGatewayUiState = MutableStateFlow<DetailGatewayUiState>(DetailGatewayUiState.Empty)
    val detailGatewayUiState = _detailGatewayUiState.asStateFlow()

    init {
        viewModelScope.launch {
            gatewayRepository.retrieveOne(hrefGateway)?.collect { apiResult ->
                _detailGatewayUiState.update { _ ->
                    when(apiResult) {
                        is ApiResult.Error -> DetailGatewayUiState.Error(apiResult.exception)
                        ApiResult.Loading -> DetailGatewayUiState.Loading
                        is ApiResult.Success -> DetailGatewayUiState.Success(apiResult.data)
                    }
                }
            }
        }
    }

    class Factory(private val hrefGateway:String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(String::class.java).newInstance(hrefGateway)
        }
    }
}