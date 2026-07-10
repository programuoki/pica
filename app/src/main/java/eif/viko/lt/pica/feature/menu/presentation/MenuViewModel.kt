package eif.viko.lt.pica.feature.menu.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eif.viko.lt.pica.feature.menu.data.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MenuViewModel(
    private val repository: MenuRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MenuUiState>(MenuUiState.Loading)
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    init {
        observeMenu()
        refresh()
    }

    fun onEvent(event: MenuEvent) {
        when (event) {
            MenuEvent.LoadMenu -> refresh()
            MenuEvent.Retry -> refresh()
        }
    }

    // Observe the cache — emits instantly with cached data, re-emits when it changes
    private fun observeMenu() {
        viewModelScope.launch {
            repository.getMenu().collect { items ->
                if (items.isNotEmpty()) {
                    _uiState.value = MenuUiState.Success(items)
                }
            }
        }
    }

    // Fetch from network → write to cache → the Flow above auto-emits the update
    private fun refresh() {
        viewModelScope.launch {
            try {
                repository.refresh()
            } catch (e: Exception) {
                // Only show error if there's no cached data already showing
                if (_uiState.value !is MenuUiState.Success) {
                    _uiState.value = MenuUiState.Error(e.message ?: "Could not load menu")
                }
            }
        }
    }
}