package eif.viko.lt.pica.core.data.network

import eif.viko.lt.pica.feature.menu.data.MenuItemDto
import kotlinx.serialization.SerialName
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

    @POST("orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): CreateOrderResponse

    @GET("orders")
    suspend fun getOrders(): List<OrderDto>

    @POST("create-payment-intent")
    suspend fun createPaymentIntent(@Body request: PaymentIntentRequest): PaymentIntentResponse



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
@kotlinx.serialization.Serializable
data class CreateOrderRequest(
    val items: List<OrderItemRequest>,
    @SerialName("table_number") val tableNumber: String? = null
)

@kotlinx.serialization.Serializable
data class OrderItemRequest(
    @kotlinx.serialization.SerialName("menu_item_id") val menuItemId: Int,
    val name: String,
    @kotlinx.serialization.SerialName("price_cents") val priceCents: Int,
    val quantity: Int
)

@kotlinx.serialization.Serializable
data class CreateOrderResponse(
    val id: Int,
    @kotlinx.serialization.SerialName("total_cents") val totalCents: Int,
    val status: String
)




@kotlinx.serialization.Serializable
data class OrderDto(
    val id: Int,
    @kotlinx.serialization.SerialName("total_cents") val totalCents: Int,
    val status: String,
    @kotlinx.serialization.SerialName("created_at") val createdAt: String
)

@kotlinx.serialization.Serializable
data class PaymentIntentRequest(
    @kotlinx.serialization.SerialName("amount_cents") val amountCents: Int
)

@kotlinx.serialization.Serializable
data class PaymentIntentResponse(
    @kotlinx.serialization.SerialName("client_secret") val clientSecret: String
)