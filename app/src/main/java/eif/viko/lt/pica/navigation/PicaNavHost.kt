package eif.viko.lt.pica.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import eif.viko.lt.pica.feature.auth.presentation.LoginScreen
import eif.viko.lt.pica.feature.cart.presentation.CartScreen
import eif.viko.lt.pica.feature.cart.presentation.CartViewModel
import eif.viko.lt.pica.feature.menu.presentation.MenuScreen
import org.koin.compose.koinInject

@Composable
fun PicaNavHost() {
    val backStack = rememberNavBackStack(Screen.Login)   // ← start at Login

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Screen.Login> {
                LoginScreen(
                    onLoginSuccess = {
                        // replace Login with Menu so back button doesn't return to login
                        backStack.clear()
                        backStack.add(Screen.Menu)
                    }
                )
            }
            entry<Screen.Menu> {
                MenuScreen()
            }
            entry<Screen.Cart> {
                val cartVM: CartViewModel = koinInject()
                CartScreen(
                    viewModel = cartVM,
                    onCheckout = { /* → Screen.Checkout later */ }
                )
            }

        }
    )
}