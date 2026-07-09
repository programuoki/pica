package eif.viko.lt.pica.feature.menu.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuItemDto(
    val id: Int,
    val name: String,
    val description: String,
    @SerialName("price_cents") val priceCents: Int,
    val category: String,
    @SerialName("image_url") val imageUrl: String?
)

// DTO → domain mapper (kept simple, as an extension function)
fun MenuItemDto.toDomain(): MenuItem = MenuItem(
    id = id,
    name = name,
    description = description,
    price = priceCents / 100.0,   // cents → euros
    category = category,
    imageUrl = imageUrl
)