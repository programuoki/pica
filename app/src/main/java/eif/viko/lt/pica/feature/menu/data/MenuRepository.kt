package eif.viko.lt.pica.feature.menu.data

import eif.viko.lt.pica.core.data.local.MenuDao
import eif.viko.lt.pica.core.data.local.MenuItemEntity
import eif.viko.lt.pica.core.data.network.PicaApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MenuRepository(
    private val api: PicaApi,
    private val dao: MenuDao
) {
    // The cache is the single source of truth — UI observes this Flow
    fun getMenu(): Flow<List<MenuItem>> =
        dao.getMenu().map { entities -> entities.map { it.toDomain() } }

    // Fetch from network, write to cache — the Flow above auto-emits the update
    suspend fun refresh() {
        val fromNetwork = api.getMenu().map { it.toDomain() }
        dao.clearAll()
        dao.insertAll(fromNetwork.map { it.toEntity() })
    }
}

// mappers: entity ↔ domain
private fun MenuItemEntity.toDomain() = MenuItem(id, name, description, price, category, imageUrl)
private fun MenuItem.toEntity() = MenuItemEntity(id, name, description, price, category, imageUrl)