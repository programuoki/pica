package eif.viko.lt.pica.feature.menu.di

import eif.viko.lt.pica.feature.menu.data.MenuRepository
import eif.viko.lt.pica.feature.menu.presentation.MenuViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val menuModule = module {
    // Repository — Koin injects PicaApi (provided by coreModule)
    single { MenuRepository(get()) }

    // ViewModel — Koin injects the repository
    viewModel { MenuViewModel(get()) }
}