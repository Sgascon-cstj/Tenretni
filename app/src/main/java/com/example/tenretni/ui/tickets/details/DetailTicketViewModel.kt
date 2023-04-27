package com.example.tenretni.ui.tickets.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tenretni.core.ApiResult
import com.example.tenretni.data.repositories.CustomerRepository
import com.example.tenretni.data.repositories.TicketRepository
import com.example.tenretni.domain.models.Ticket
import com.example.tenretni.ui.tickets.TicketUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailTicketViewModel(private val href: String): ViewModel() {

    private val ticketRepository = TicketRepository()
    private val customerRepository = CustomerRepository()

    private val _detailTicketUiState = MutableStateFlow<DetailTicketUiState>(DetailTicketUiState.Empty)
    val detailTicketUiState = _detailTicketUiState.asStateFlow()

    init {
        loadInformation()
    }
    fun loadInformation(){
        viewModelScope.launch {
            ticketRepository.retrieveOne(href)?.collect { apiResult->
                _detailTicketUiState.update {
                    when(apiResult){
                        is ApiResult.Error -> DetailTicketUiState.Error(apiResult.exception)
                        ApiResult.Loading -> DetailTicketUiState.Loading
                        is ApiResult.Success -> {
                            retrieveGateways(apiResult.data)
                            DetailTicketUiState.TicketSuccess(apiResult.data)
                        }
                    }
                }
            }
        }
    }
    fun retrieveGateways(ticket: Ticket){
        viewModelScope.launch {
            customerRepository.retrieveGateways(ticket.customer.href)?.collect{apiResult->
                _detailTicketUiState.update {
                    when(apiResult){
                        is ApiResult.Error -> DetailTicketUiState.Error(apiResult.exception)
                        ApiResult.Loading -> DetailTicketUiState.Loading
                        is ApiResult.Success -> DetailTicketUiState.GatewaysSuccess(apiResult.data)
                    }
                }
            }
        }
    }
    class Factory(private val href: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(String::class.java).newInstance(href)
        }
    }
}