package eif.viko.lt.pica.feature.cart.data

import eif.viko.lt.pica.feature.menu.data.MenuItem

data class CartItem(
    val menuItem: MenuItem,
    val quantity: Int
)