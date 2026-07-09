package eif.viko.lt.pica.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import eif.viko.lt.pica.feature.auth.presentation.LoginScreen
import eif.viko.lt.pica.feature.menu.presentation.MenuScreen

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
        }
    )
}