package eif.viko.lt.pica.feature.menu.data

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class MenuRepository {

    // Hardcoded for now — swap for PicaApi.getMenu() once the backend exists
    suspend fun getMenu(): List<MenuItem> {
        delay(800.milliseconds) // simulate network latency, so you can see the Loading state
        return listOf(
            MenuItem(1, "Margherita", "Tomato, mozzarella, basil", 8.50, "Pizza", null),
            MenuItem(2, "Pepperoni", "Pepperoni, mozzarella, tomato", 9.90, "Pizza", null),
            MenuItem(3, "Quattro Formaggi", "Four cheeses", 10.50, "Pizza", null),
            MenuItem(4, "Coca-Cola", "0.5L", 2.00, "Drinks", null),
            MenuItem(5, "Tiramisu", "Classic Italian dessert", 4.50, "Dessert", null),
        )
    }
}