package eif.viko.lt.pica.feature.auth.presentation

sealed interface AuthEvent {
    data class EmailChanged(val value: String) : AuthEvent
    data class PasswordChanged(val value: String) : AuthEvent
    data object Submit : AuthEvent          // ← one submit, behavior depends on mode
    data object ToggleMode : AuthEvent      // ← switch login ↔ register
}