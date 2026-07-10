package eif.viko.lt.pica

import android.app.Application
import com.stripe.android.PaymentConfiguration
import eif.viko.lt.pica.core.di.coreModule
import eif.viko.lt.pica.feature.auth.di.authModule
import eif.viko.lt.pica.feature.cart.di.cartModule
import eif.viko.lt.pica.feature.menu.di.menuModule
import eif.viko.lt.pica.feature.orders.di.orderHistoryModule
import eif.viko.lt.pica.feature.scan.di.scanModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PicaApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51TrWpBAu7YY4Xv8a7ekDIXlB2bvFyJ8hK8CUqZug0J3psgbG03Oa63SMNhxVaJyRtOH5Q6eQdHIUsz9cXEIT1tDO00piLXmDXF"   // your publishable key from the Stripe dashboard
        )

        startKoin {
            androidLogger()
            androidContext(this@PicaApplication)
            modules(
                coreModule,
                menuModule,
                authModule,
                cartModule,
                orderHistoryModule,
                scanModule
            )
        }
    }
}