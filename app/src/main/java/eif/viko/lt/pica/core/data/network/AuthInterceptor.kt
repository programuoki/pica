package eif.viko.lt.pica.core.data.network


import eif.viko.lt.pica.core.data.auth.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenStorage: TokenStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val path = originalRequest.url.encodedPath

        // Endpoints that must NOT carry a token (no token exists yet)
        val publicPaths = listOf("/login", "/register", "/auth/google")
        if (publicPaths.any { path.contains(it) }) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking { tokenStorage.getToken() }
        val newRequest = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }
        return chain.proceed(newRequest)
    }
}