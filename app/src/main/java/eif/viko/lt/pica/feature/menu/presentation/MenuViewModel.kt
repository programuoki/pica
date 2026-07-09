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
        onEvent(MenuEvent.LoadMenu)   // load automatically when created
    }

    fun onEvent(event: MenuEvent) {
        when (event) {
            MenuEvent.LoadMenu -> loadMenu()
            MenuEvent.Retry -> loadMenu()
        }
    }

    private fun loadMenu() {
        viewModelScope.launch {
            _uiState.value = MenuUiState.Loading
            try {
                val items = repository.getMenu()
                _uiState.value = MenuUiState.Success(items)
            } catch (e: Exception) {
                _uiState.value = MenuUiState.Error(
                    e.message ?: "Could not load menu"
                )
            }
        }
    }
}