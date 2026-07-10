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
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet

@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onOrderPlaced: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Stripe PaymentSheet
    val paymentSheet = rememberPaymentSheet { result ->
        when (result) {
            is PaymentSheetResult.Completed -> viewModel.onEvent(CartEvent.PaymentSucceeded)
            is PaymentSheetResult.Canceled -> viewModel.onEvent(CartEvent.PaymentCanceled)
            is PaymentSheetResult.Failed -> viewModel.onEvent(CartEvent.PaymentFailed(result.error.message))
        }
    }

    // When we get a client secret → present the payment sheet
    LaunchedEffect(state.clientSecret) {
        state.clientSecret?.let { secret ->
            paymentSheet.presentWithPaymentIntent(
                secret,
                PaymentSheet.Configuration(merchantDisplayName = "Pica")
            )
        }
    }

    // Order placed → snackbar, reset, navigate back
    LaunchedEffect(state.orderPlaced) {
        if (state.orderPlaced) {
            snackbarHostState.showSnackbar("Payment complete! 🍕")
            viewModel.onEvent(CartEvent.ClearOrderPlaced)
            onOrderPlaced()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        if (state.isPlacingOrder || state.clientSecret != null || state.orderPlaced) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(16.dp))
                    Text("Processing payment…")
                }
            }
            return@Scaffold
        }

        if (state.isEmpty) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                Text("Your cart is empty")
            }
            return@Scaffold
        }

        // 3. Cart with items
        Column(Modifier.fillMaxSize().padding(padding)) {
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

            if (state.error != null) {
                Text(
                    state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }

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
                    Button(
                        onClick = { viewModel.onEvent(CartEvent.Checkout) },
                        enabled = !state.isPlacingOrder
                    ) {
                        Text("Pay €%.2f".format(state.totalPrice))
                    }
                }
            }
        }
    }
}