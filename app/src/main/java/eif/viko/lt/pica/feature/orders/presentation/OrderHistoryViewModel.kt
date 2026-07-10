package eif.viko.lt.pica.feature.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eif.viko.lt.pica.feature.orders.data.OrderHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderHistoryViewModel(
    private val repository: OrderHistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrderHistoryUiState>(OrderHistoryUiState.Loading)
    val uiState: StateFlow<OrderHistoryUiState> = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = OrderHistoryUiState.Loading
            try {
                val orders = repository.getOrders()
                _uiState.value = OrderHistoryUiState.Success(orders)
            } catch (e: Exception) {
                _uiState.value = OrderHistoryUiState.Error(
                    e.message ?: "Could not load orders"
                )
            }
        }
    }
}