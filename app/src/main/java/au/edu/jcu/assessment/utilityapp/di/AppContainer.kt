package au.edu.jcu.assessment.utilityapp.di

import au.edu.jcu.assessment.utilityapp.data.NetworkQuoteRepository
import au.edu.jcu.assessment.utilityapp.data.QuoteRepository
import au.edu.jcu.assessment.utilityapp.data.remote.QuoteApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://zenquotes.io/"
private const val TIMEOUT_SECONDS = 10L

/** Manual dependency injection: one place that knows how to build the app's dependencies. */
interface AppContainer {
    val quoteRepository: QuoteRepository
}

class DefaultAppContainer : AppContainer {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val quoteApi: QuoteApi by lazy { retrofit.create(QuoteApi::class.java) }

    override val quoteRepository: QuoteRepository by lazy { NetworkQuoteRepository(quoteApi) }
}
