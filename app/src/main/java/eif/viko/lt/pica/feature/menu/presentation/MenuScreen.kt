package eif.viko.lt.pica.feature.menu.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eif.viko.lt.pica.feature.cart.presentation.CartEvent
import eif.viko.lt.pica.feature.cart.presentation.CartViewModel
import eif.viko.lt.pica.feature.scan.data.TableSession
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    viewModel: MenuViewModel = koinViewModel(),
    cartViewModel: CartViewModel = koinInject(),
    tableSession: TableSession = koinInject(),
    onCartClick: () -> Unit = {},
    onOrdersClick: () -> Unit = {},
    onScanClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val cartState by cartViewModel.uiState.collectAsStateWithLifecycle()
    val table by tableSession.tableNumber.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (table != null) "Pica · Table $table" else "Pica Menu") },
                actions = {
                    // Scan table button
                    IconButton(onClick = onScanClick) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan table")
                    }
                    // Order history button
                    IconButton(onClick = onOrdersClick) {
                        Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = "My orders")
                    }
                    // Logout button
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Log out")
                    }
                    // Cart button with badge
                    BadgedBox(
                        badge = {
                            if (cartState.itemCount > 0) {
                                Badge { Text("${cartState.itemCount}") }
                            }
                        },
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        IconButton(onClick = onCartClick) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (val s = state) {
                is MenuUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is MenuUiState.Error -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Error: ${s.message}")
                            Spacer(Modifier.height(12.dp))
                            Button(onClick = { viewModel.onEvent(MenuEvent.Retry) }) {
                                Text("Retry")
                            }
                        }
                    }
                }

                is MenuUiState.Success -> {
                    LazyColumn(
                        Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(s.items) { item ->
                            Card(Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(16.dp)) {
                                    Text(item.name, style = MaterialTheme.typography.titleMedium)
                                    Text(item.description, style = MaterialTheme.typography.bodyMedium)
                                    Text(
                                        "€%.2f".format(item.price),
                                        style = MaterialTheme.typography.titleSmall
                                    )

                                    Spacer(Modifier.height(8.dp))

                                    Button(
                                        onClick = { cartViewModel.onEvent(CartEvent.AddItem(item)) },
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Text("Add to cart")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}