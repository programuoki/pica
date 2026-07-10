package eif.viko.lt.pica.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen : NavKey {

    @Serializable data object Menu : Screen()
    @Serializable data class MenuItemDetail(val itemId: Int) : Screen()
    @Serializable data object Cart : Screen()
    @Serializable data object Checkout : Screen()
    @Serializable data object Login : Screen()
    @Serializable data object OrderHistory : Screen()
    @Serializable data object Scan : Screen()
}