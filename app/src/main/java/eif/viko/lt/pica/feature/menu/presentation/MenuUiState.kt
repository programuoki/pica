package eif.viko.lt.pica.feature.menu.presentation

import eif.viko.lt.pica.feature.menu.data.MenuItem

sealed interface MenuUiState {
    data object Loading : MenuUiState
    data class Success(val items: List<MenuItem>) : MenuUiState
    data class Error(val message: String) : MenuUiState
}