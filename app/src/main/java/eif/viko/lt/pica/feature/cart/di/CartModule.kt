package eif.viko.lt.pica.feature.cart.di

import eif.viko.lt.pica.feature.cart.data.OrderRepository
import eif.viko.lt.pica.feature.cart.presentation.CartViewModel
import org.koin.dsl.module

val cartModule = module {
    single { OrderRepository(get()) }
    single { CartViewModel(get(), get(), get()) }   // OrderRepository, PicaApi, TableSession
}