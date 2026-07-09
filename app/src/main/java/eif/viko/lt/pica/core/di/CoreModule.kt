package eif.viko.lt.pica.core.di

import eif.viko.lt.pica.core.data.network.PicaApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val coreModule = module {
    single<Json> {
        Json { ignoreUnknownKeys = true }
    }
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")  // 10.0.2.2 = localhost from emulator
            .client(get())
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .build()
    }
    single<PicaApi> { get<Retrofit>().create(PicaApi::class.java) }
}
