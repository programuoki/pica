package eif.viko.lt.pica.feature.orders.data

data class Order(
    val id: Int,
    val total: Double,
    val status: String,
    val createdAt: String
)