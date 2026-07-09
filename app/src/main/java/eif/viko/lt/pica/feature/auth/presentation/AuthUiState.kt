package eif.viko.lt.pica.feature.auth.presentation

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val isLoginMode: Boolean = true   // ← true = Log in, false = Sign up
)