package com.example.tenretni.ui.tickets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenretni.core.ApiResult
import com.example.tenretni.data.repositories.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TicketsViewModel : ViewModel() {

    private val ticketRepository = TicketRepository()

    private val _ticketUiState = MutableStateFlow<TicketUiState>(TicketUiState.Empty)
    val ticketUiState = _ticketUiState.asStateFlow()

    // TODO: Il va falloir parler au repo est creer retrieveAll
    init {
        viewModelScope.launch {
            ticketRepository.retrieveAll().collect {apiResult ->
                _ticketUiState.update { _ ->
                    when(apiResult) {
                        is ApiResult.Error -> TicketUiState.Error(apiResult.exception)
                        ApiResult.Loading -> TicketUiState.Loading
                        is ApiResult.Success -> TicketUiState.Success(apiResult.data)
                    }
                }
            }
        }
    }

}