package eif.viko.lt.pica.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eif.viko.lt.pica.feature.auth.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface StartupState {
    data object Checking : StartupState
    data object LoggedIn : StartupState
    data object LoggedOut : StartupState
}

class StartupViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<StartupState>(StartupState.Checking)
    val state: StateFlow<StartupState> = _state.asStateFlow()

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            _state.value = if (repository.isLoggedIn()) {
                StartupState.LoggedIn
            } else {
                StartupState.LoggedOut
            }
        }
    }
}