package eif.viko.lt.pica.feature.auth.di

import eif.viko.lt.pica.feature.auth.data.AuthRepository
import eif.viko.lt.pica.feature.auth.presentation.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single { AuthRepository(get(), get()) }   // get() = PicaApi, get() = TokenStorage
    viewModel { AuthViewModel(get()) }
}