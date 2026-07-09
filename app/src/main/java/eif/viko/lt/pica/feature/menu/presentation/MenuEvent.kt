package eif.viko.lt.pica.feature.menu.presentation

sealed interface MenuEvent {
    data object LoadMenu : MenuEvent
    data object Retry : MenuEvent
}