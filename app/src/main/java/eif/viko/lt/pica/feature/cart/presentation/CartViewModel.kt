package eif.viko.lt.pica.feature.cart.presentation

import androidx.lifecycle.ViewModel
import eif.viko.lt.pica.feature.cart.data.CartItem
import eif.viko.lt.pica.feature.menu.data.MenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.AddItem -> addItem(event.menuItem)
            is CartEvent.RemoveItem -> removeItem(event.menuItem)
            is CartEvent.IncreaseQuantity -> changeQuantity(event.menuItem, +1)
            is CartEvent.DecreaseQuantity -> changeQuantity(event.menuItem, -1)
            CartEvent.ClearCart -> _uiState.update { it.copy(items = emptyList()) }
        }
    }

    private fun addItem(menuItem: MenuItem) {
        _uiState.update { state ->
            val existing = state.items.find { it.menuItem.id == menuItem.id }
            val newItems = if (existing != null) {
                // already in cart → bump quantity
                state.items.map {
                    if (it.menuItem.id == menuItem.id) it.copy(quantity = it.quantity + 1)
                    else it
                }
            } else {
                // new → add with quantity 1
                state.items + CartItem(menuItem, 1)
            }
            state.copy(items = newItems)
        }
    }

    private fun changeQuantity(menuItem: MenuItem, delta: Int) {
        _uiState.update { state ->
            val newItems = state.items
                .map {
                    if (it.menuItem.id == menuItem.id) it.copy(quantity = it.quantity + delta)
                    else it
                }
                .filter { it.quantity > 0 }   // quantity 0 → remove from cart
            state.copy(items = newItems)
        }
    }

    private fun removeItem(menuItem: MenuItem) {
        _uiState.update { state ->
            state.copy(items = state.items.filter { it.menuItem.id != menuItem.id })
        }
    }
}