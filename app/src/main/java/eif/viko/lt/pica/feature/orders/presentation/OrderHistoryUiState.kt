package eif.viko.lt.pica.feature.orders.presentation

import eif.viko.lt.pica.feature.orders.data.Order

sealed interface OrderHistoryUiState {
    data object Loading : OrderHistoryUiState
    data class Success(val orders: List<Order>) : OrderHistoryUiState
    data class Error(val message: String) : OrderHistoryUiState
}