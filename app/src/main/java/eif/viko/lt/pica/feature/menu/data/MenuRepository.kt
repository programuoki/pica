package eif.viko.lt.pica.feature.menu.data

import eif.viko.lt.pica.core.data.network.PicaApi


class MenuRepository(private val api: PicaApi) {
    suspend fun getMenu(): List<MenuItem> =
        api.getMenu().map { it.toDomain() }
}