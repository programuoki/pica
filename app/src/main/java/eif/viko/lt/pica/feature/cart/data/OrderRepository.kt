package eif.viko.lt.pica.feature.cart.data

import eif.viko.lt.pica.core.data.network.CreateOrderRequest
import eif.viko.lt.pica.core.data.network.OrderItemRequest
import eif.viko.lt.pica.core.data.network.PicaApi

class OrderRepository(
    private val api: PicaApi
) {
    suspend fun placeOrder(items: List<CartItem>, tableNumber: String?) {
        val request = CreateOrderRequest(
            items = items.map { cartItem ->
                OrderItemRequest(
                    menuItemId = cartItem.menuItem.id,
                    name = cartItem.menuItem.name,
                    priceCents = (cartItem.menuItem.price * 100).toInt(),
                    quantity = cartItem.quantity
                )
            },
            tableNumber = tableNumber          // ← ADD
        )
        api.createOrder(request)
    }
}