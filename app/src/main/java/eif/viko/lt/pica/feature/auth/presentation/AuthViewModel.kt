package eif.viko.lt.pica.feature.auth.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eif.viko.lt.pica.feature.auth.data.AuthRepository
import eif.viko.lt.pica.feature.scan.data.TableSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    private val tableSession: TableSession
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EmailChanged ->
                _uiState.update { it.copy(email = event.value, error = null) }
            is AuthEvent.PasswordChanged ->
                _uiState.update { it.copy(password = event.value, error = null) }
            AuthEvent.ToggleMode ->
                _uiState.update { it.copy(isLoginMode = !it.isLoginMode, error = null) }
            AuthEvent.Submit ->
                authenticate(isLogin = _uiState.value.isLoginMode)   // ← mode decides
        }
    }

    private fun authenticate(isLogin: Boolean) {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(error = "Email and password required") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                if (isLogin) repository.login(state.email, state.password)
                else repository.register(state.email, state.password)
                _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Login failed. Check your credentials.")
                }
            }
        }
    }

    fun googleSignIn(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.googleSignIn(idToken)
                _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Google sign-in failed") }
            }
        }
    }


    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            repository.logout()   // clears the token from SecureTokenStorage
            tableSession.clearTable()    // ← clear table on logout
            onLoggedOut()         // navigate back to login
        }
    }
}