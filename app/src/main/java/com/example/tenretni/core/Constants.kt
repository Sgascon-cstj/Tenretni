package com.example.tenretni.core

object Constants {

    object BaseURL {
        //private const val BASE_API = "http://10.0.2.2:5000"
        private const val BASE_API = "https://api.andromia.science"
        const val TICKETS = "$BASE_API/tickets"
        const val CUSTOMERS = "$BASE_API/customers"
        const val CUSTOMERS_GATEWAYS = "%s/gateways"
        const val GATEWAYS = "$BASE_API/gateways"
        const val NETWORK = "$BASE_API/network"
    }

    const val FLAG_API_URL = "https://flagcdn.com/h40/%s.png"

    const val LOADING_DELAY_MS = 10000L // 10 secondes
    const val COLUMNS_GATEWAYS = 2
    object RefreshDelay{
        const val GATEWAY_LIST = 60 * 1000L
        const val TICKETS_LIST = 30 * 1000L
    }
    enum class TicketPriority {
        Low, Normal, High, Critical
    }

    enum class TicketStatus {
        Open, Solved
    }

    enum class ConnectionStatus {
        Online, Offline
    }

}