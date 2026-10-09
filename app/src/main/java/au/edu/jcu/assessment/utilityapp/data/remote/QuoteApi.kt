package au.edu.jcu.assessment.utilityapp.data.remote

import retrofit2.http.GET

/** Retrofit description of the ZenQuotes web API (https://zenquotes.io). */
interface QuoteApi {

    /** The quote of the day. Returns a list with a single item. */
    @GET("api/today")
    suspend fun getQuoteOfTheDay(): List<QuoteDto>

    /** A random quote. Returns a list with a single item. */
    @GET("api/random")
    suspend fun getRandomQuote(): List<QuoteDto>
}
