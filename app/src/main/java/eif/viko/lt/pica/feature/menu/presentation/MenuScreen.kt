package eif.viko.lt.pica.feature.menu.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun MenuScreen(
    viewModel: MenuViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

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
                            Text("€%.2f".format(item.price), style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }
            }
        }
    }
}