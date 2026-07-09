package eif.viko.lt.pica.feature.cart.presentation

import eif.viko.lt.pica.feature.cart.data.CartItem

data class CartUiState(
    val items: List<CartItem> = emptyList()
) {
    // Computed properties — derived from items, always in sync
    val totalPrice: Double
        get() = items.sumOf { it.menuItem.price * it.quantity }

    val itemCount: Int
        get() = items.sumOf { it.quantity }

    val isEmpty: Boolean
        get() = items.isEmpty()
}