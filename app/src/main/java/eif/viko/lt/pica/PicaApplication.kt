package eif.viko.lt.pica

import android.app.Application
import eif.viko.lt.pica.core.di.coreModule
import eif.viko.lt.pica.feature.auth.di.authModule
import eif.viko.lt.pica.feature.menu.di.menuModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PicaApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PicaApplication)
            modules(
                coreModule,
                menuModule,
                authModule
            )
        }
    }
}