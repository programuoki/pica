package eif.viko.lt.pica.feature.auth.data

import eif.viko.lt.pica.core.data.auth.TokenStorage
import eif.viko.lt.pica.core.data.network.AuthRequest
import eif.viko.lt.pica.core.data.network.GoogleAuthRequest
import eif.viko.lt.pica.core.data.network.PicaApi

class AuthRepository(
    private val api: PicaApi,
    private val tokenStorage: TokenStorage
) {
    suspend fun login(email: String, password: String) {
        val response = api.login(AuthRequest(email, password))
        tokenStorage.saveToken(response.token)   // ← store the JWT
    }

    suspend fun register(email: String, password: String) {
        val response = api.register(AuthRequest(email, password))
        tokenStorage.saveToken(response.token)
    }

    suspend fun googleSignIn(idToken: String) {
        val response = api.googleAuth(GoogleAuthRequest(idToken))
        tokenStorage.saveToken(response.token)   // same storage as email/password
    }


    suspend fun isLoggedIn(): Boolean = tokenStorage.hasValidToken()

    suspend fun logout() = tokenStorage.clearToken()
}