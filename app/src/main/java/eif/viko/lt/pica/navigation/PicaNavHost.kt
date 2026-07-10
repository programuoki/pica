package eif.viko.lt.pica.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import eif.viko.lt.pica.feature.auth.presentation.AuthViewModel
import eif.viko.lt.pica.feature.auth.presentation.LoginScreen
import eif.viko.lt.pica.feature.auth.presentation.StartupState
import eif.viko.lt.pica.feature.auth.presentation.StartupViewModel
import eif.viko.lt.pica.feature.cart.presentation.CartScreen
import eif.viko.lt.pica.feature.cart.presentation.CartViewModel
import eif.viko.lt.pica.feature.menu.presentation.MenuScreen
import eif.viko.lt.pica.feature.orders.presentation.OrderHistoryScreen
import eif.viko.lt.pica.feature.scan.data.TableSession
import eif.viko.lt.pica.feature.scan.presentation.ScanScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun PicaNavHost(
    startupViewModel: StartupViewModel = koinViewModel(),
    tableSession: TableSession = koinInject()
) {
    val startupState by startupViewModel.state.collectAsStateWithLifecycle()
    val table by tableSession.tableNumber.collectAsStateWithLifecycle()

    if (startupState == StartupState.Checking) {
        Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
        return
    }

    val startScreen = when {
        startupState != StartupState.LoggedIn -> Screen.Login
        table == null -> Screen.Scan
        else -> Screen.Menu
    }
    val backStack = rememberNavBackStack(startScreen)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Screen.Login> {
                LoginScreen(onLoginSuccess = {
                    backStack.clear()
                    backStack.add(Screen.Scan)      // login → scan
                })
            }
            entry<Screen.Scan> {
                ScanScreen(onTableScanned = { raw ->
                    tableSession.setTable(raw)
                    backStack.clear()
                    backStack.add(Screen.Menu)      // scan → menu
                })
            }
            entry<Screen.Menu> {
                val authVM: AuthViewModel = koinViewModel()
                MenuScreen(
                    onCartClick = { backStack.add(Screen.Cart) },
                    onOrdersClick = { backStack.add(Screen.OrderHistory) },
                    onLogout = {
                        authVM.logout(onLoggedOut = {
                            backStack.clear()
                            backStack.add(Screen.Login)   // logout clears table → next login re-scans
                        })
                    }
                )
            }
            entry<Screen.Cart> {
                val cartVM: CartViewModel = koinInject()
                CartScreen(viewModel = cartVM, onOrderPlaced = {
                    backStack.clear()
                    backStack.add(Screen.Menu)      // after pay → menu (table still set)
                })
            }
            entry<Screen.OrderHistory> {
                OrderHistoryScreen()
            }
        }
    )
}