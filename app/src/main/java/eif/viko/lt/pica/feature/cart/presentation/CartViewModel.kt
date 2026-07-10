package eif.viko.lt.pica.feature.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eif.viko.lt.pica.core.data.network.PaymentIntentRequest
import eif.viko.lt.pica.core.data.network.PicaApi
import eif.viko.lt.pica.feature.cart.data.CartItem
import eif.viko.lt.pica.feature.cart.data.OrderRepository
import eif.viko.lt.pica.feature.menu.data.MenuItem
import eif.viko.lt.pica.feature.scan.data.TableSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val orderRepository: OrderRepository,
    private val api: PicaApi,
    private val tableSession: TableSession
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.AddItem -> addItem(event.menuItem)
            is CartEvent.RemoveItem -> removeItem(event.menuItem)
            is CartEvent.IncreaseQuantity -> changeQuantity(event.menuItem, +1)
            is CartEvent.DecreaseQuantity -> changeQuantity(event.menuItem, -1)
            CartEvent.ClearCart -> _uiState.update { it.copy(items = emptyList()) }
            CartEvent.Checkout -> startPayment()
            CartEvent.PaymentSucceeded -> completeOrder()
            CartEvent.PaymentCanceled ->
                _uiState.update { it.copy(clientSecret = null, isPlacingOrder = false) }
            is CartEvent.PaymentFailed ->
                _uiState.update { it.copy(clientSecret = null, isPlacingOrder = false, error = event.message ?: "Payment failed") }
            CartEvent.ClearOrderPlaced ->
                _uiState.update { it.copy(orderPlaced = false, clientSecret = null) }

        }
    }

    // Step 1: ask backend for a PaymentIntent client secret
    private fun startPayment() {
        val state = _uiState.value
        if (state.items.isEmpty()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isPlacingOrder = true, error = null) }
            try {
                val response = api.createPaymentIntent(PaymentIntentRequest(state.totalCents))
                _uiState.update { it.copy(clientSecret = response.clientSecret) }
                // clientSecret set → the screen's LaunchedEffect presents the PaymentSheet
            } catch (e: Exception) {
                _uiState.update { it.copy(isPlacingOrder = false, error = "Could not start payment") }
            }
        }
    }

    // Step 2: payment succeeded → NOW create the order in our DB
    // In completeOrder() — DON'T clear isPlacingOrder until orderPlaced is set together
    private fun completeOrder() {
        val items = _uiState.value.items
        val table = tableSession.tableNumber.value
        viewModelScope.launch {
            try {
                orderRepository.placeOrder(items, table)
                // NO clearTable() — table persists for more orders
                _uiState.update {
                    it.copy(
                        items = emptyList(),
                        isPlacingOrder = false,
                        orderPlaced = true,
                        clientSecret = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isPlacingOrder = false, error = "Order failed") }
            }
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