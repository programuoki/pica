package eif.viko.lt.pica.feature.orders.di

import eif.viko.lt.pica.feature.orders.data.OrderHistoryRepository
import eif.viko.lt.pica.feature.orders.presentation.OrderHistoryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val orderHistoryModule = module {
    single { OrderHistoryRepository(get()) }
    viewModel { OrderHistoryViewModel(get()) }
}