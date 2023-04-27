package com.example.tenretni.ui.tickets.details

import com.example.tenretni.domain.models.Customer
import com.example.tenretni.domain.models.Ticket

sealed class DetailTicketUiState {
    object Empty : DetailTicketUiState()
    object Loading: DetailTicketUiState()
    class Error(val e: Exception) : DetailTicketUiState()
    class TicketSuccess(val ticket: Ticket) : DetailTicketUiState()
    class CustumerSuccess(val customer: Customer) : DetailTicketUiState()
}