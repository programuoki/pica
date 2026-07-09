package eif.viko.lt.pica.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import eif.viko.lt.pica.feature.menu.presentation.MenuScreen

@Composable
fun PicaNavHost() {
    val backStack = rememberNavBackStack(Screen.Menu)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Screen.Menu> {
                MenuScreen()
            }
            // entry<Screen.Cart> { CartScreen() }
            // entry<Screen.MenuItemDetail> { key -> DetailScreen(key.itemId) }
        }
    )
}