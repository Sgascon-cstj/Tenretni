package com.example.tenretni.ui.tickets

import com.example.tenretni.domain.models.Ticket
import java.lang.Exception

sealed class TicketUiState {
    object Empty : TicketUiState()
    object Loading : TicketUiState()
    class Success(val tickets: List<Ticket>) : TicketUiState()
    class Error(val exception: Exception? = null) : TicketUiState()
}