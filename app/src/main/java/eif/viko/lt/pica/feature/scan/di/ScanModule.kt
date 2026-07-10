package eif.viko.lt.pica.feature.scan.di

import eif.viko.lt.pica.feature.scan.data.TableSession
import org.koin.dsl.module

val scanModule = module{
    single { TableSession() }
}