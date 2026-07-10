package eif.viko.lt.pica.feature.orders.data

import eif.viko.lt.pica.core.data.network.PicaApi

class OrderHistoryRepository(
    private val api: PicaApi
) {
    suspend fun getOrders(): List<Order> =
        api.getOrders().map { dto ->
            Order(
                id = dto.id,
                total = dto.totalCents / 100.0,   // cents → euros
                status = dto.status,
                createdAt = dto.createdAt
            )
        }
}