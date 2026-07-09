package eif.viko.lt.pica.feature.cart.presentation


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CartScreen(
    viewModel: CartViewModel,          // shared instance passed in (see nav wiring)
    onCheckout: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isEmpty) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text("Your cart is empty")
        }
        return
    }

    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier.weight(1f).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.items) { cartItem ->
                Card(Modifier.fillMaxWidth()) {
                    Row(
                        Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(cartItem.menuItem.name, style = MaterialTheme.typography.titleMedium)
                            Text("€%.2f".format(cartItem.menuItem.price))
                        }
                        // quantity stepper
                        IconButton(onClick = {
                            viewModel.onEvent(CartEvent.DecreaseQuantity(cartItem.menuItem))
                        }) { Text("−") }
                        Text("${cartItem.quantity}")
                        IconButton(onClick = {
                            viewModel.onEvent(CartEvent.IncreaseQuantity(cartItem.menuItem))
                        }) { Text("+") }
                    }
                }
            }
        }

        // Total + checkout bar
        Surface(tonalElevation = 3.dp) {
            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Total: €%.2f".format(state.totalPrice),
                    style = MaterialTheme.typography.titleLarge
                )
                Button(onClick = onCheckout) {
                    Text("Checkout (${state.itemCount})")
                }
            }
        }
    }
}