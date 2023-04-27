package com.example.tenretni.ui.gateways

import com.example.tenretni.domain.models.Gateway
import java.lang.Exception

sealed class GatewaysUiState {
    object Empty : GatewaysUiState()
    object Loading : GatewaysUiState()
    class Error(val ex: Exception? = null) : GatewaysUiState()
    class Success(val gateways : List<Gateway>) : GatewaysUiState()
}