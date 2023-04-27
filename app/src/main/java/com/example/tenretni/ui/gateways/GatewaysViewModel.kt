package com.example.tenretni.ui.gateways

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenretni.core.ApiResult
import com.example.tenretni.data.repositories.GatewaysRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GatewaysViewModel : ViewModel() {

    private val _gatewaysUiState = MutableStateFlow<GatewaysUiState>(GatewaysUiState.Empty)
    val gatewaysUiState = _gatewaysUiState.asStateFlow()

    private val gatewaysRepository = GatewaysRepository()

    init {
        viewModelScope.launch {
            gatewaysRepository.retriveAll().collect{apiResult->
                _gatewaysUiState.update {
                    when(apiResult){
                        is ApiResult.Error -> GatewaysUiState.Error(apiResult.exception)
                        ApiResult.Loading -> GatewaysUiState.Loading
                        is ApiResult.Success -> GatewaysUiState.Success(apiResult.data)
                    }
                }

            }
        }
    }

}