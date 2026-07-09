package eif.viko.lt.pica.feature.menu.data

data class MenuItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String?
)