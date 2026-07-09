package eif.viko.lt.pica.core.data.auth

interface TokenStorage {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
    suspend fun hasValidToken(): Boolean
}
