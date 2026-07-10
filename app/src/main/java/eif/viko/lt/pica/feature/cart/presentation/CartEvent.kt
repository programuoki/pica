package eif.viko.lt.pica.feature.cart.presentation

import eif.viko.lt.pica.feature.menu.data.MenuItem

sealed interface CartEvent {
    data class AddItem(val menuItem: MenuItem) : CartEvent
    data class RemoveItem(val menuItem: MenuItem) : CartEvent
    data class IncreaseQuantity(val menuItem: MenuItem) : CartEvent
    data class DecreaseQuantity(val menuItem: MenuItem) : CartEvent
    data object ClearCart : CartEvent
    data object Checkout : CartEvent
    data object ClearOrderPlaced : CartEvent


    data object PaymentSucceeded : CartEvent
    data object PaymentCanceled : CartEvent
    data class PaymentFailed(val message: String?) : CartEvent

}