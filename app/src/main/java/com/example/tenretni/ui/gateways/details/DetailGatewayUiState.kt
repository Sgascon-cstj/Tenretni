package com.example.tenretni.ui.gateways.details

import com.example.tenretni.domain.models.Gateway

sealed class DetailGatewayUiState {
    object Empty : DetailGatewayUiState()
    object Loading : DetailGatewayUiState()
    class Error(val ex: Exception) : DetailGatewayUiState()
    class Success(val gateway : Gateway) : DetailGatewayUiState()
}