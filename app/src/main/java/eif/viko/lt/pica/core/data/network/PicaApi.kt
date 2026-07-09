package eif.viko.lt.pica.core.data.network

import eif.viko.lt.pica.feature.menu.data.MenuItemDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PicaApi {

    @GET("menu")
    suspend fun getMenu(): List<MenuItemDto>

    @POST("register")
    suspend fun register(@Body request: AuthRequest): AuthResponse

    @POST("login")
    suspend fun login(@Body request: AuthRequest): AuthResponse
}

// Request/response bodies — match your Go backend's JSON exactly
@kotlinx.serialization.Serializable
data class AuthRequest(
    val email: String,
    val password: String
)

@kotlinx.serialization.Serializable
data class AuthResponse(
    val token: String
)