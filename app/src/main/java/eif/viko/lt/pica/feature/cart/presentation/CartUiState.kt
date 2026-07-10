package eif.viko.lt.pica.feature.cart.presentation

import eif.viko.lt.pica.feature.cart.data.CartItem
data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val isPlacingOrder: Boolean = false,
    val orderPlaced: Boolean = false,
    val error: String? = null,
    val clientSecret: String? = null       // ← triggers the PaymentSheet
) {
    val totalPrice: Double get() = items.sumOf { it.menuItem.price * it.quantity }
    val itemCount: Int get() = items.sumOf { it.quantity }
    val isEmpty: Boolean get() = items.isEmpty()
    val totalCents: Int get() = (totalPrice * 100).toInt()   // for Stripe
}