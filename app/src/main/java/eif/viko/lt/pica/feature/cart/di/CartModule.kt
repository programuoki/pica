package eif.viko.lt.pica.feature.cart.di

import eif.viko.lt.pica.feature.cart.presentation.CartViewModel
import org.koin.dsl.module

val cartModule = module {
    single { CartViewModel() }   // ← single, NOT viewModel! Shared across screens
}