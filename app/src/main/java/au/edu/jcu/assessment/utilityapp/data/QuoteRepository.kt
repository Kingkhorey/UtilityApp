package au.edu.jcu.assessment.utilityapp.data

import au.edu.jcu.assessment.utilityapp.data.local.LocalQuotes
import au.edu.jcu.assessment.utilityapp.data.model.Quote
import au.edu.jcu.assessment.utilityapp.data.model.QuoteSource
import au.edu.jcu.assessment.utilityapp.data.remote.QuoteApi
import au.edu.jcu.assessment.utilityapp.data.remote.QuoteDto
import au.edu.jcu.assessment.utilityapp.data.remote.toQuote
import java.time.LocalDate
import kotlin.coroutines.cancellation.CancellationException

/** Single source of truth for quotes. The UI never talks to the network directly. */
interface QuoteRepository {

    /** Returns a quote from [source]. Never throws: failures fall back to a saved quote. */
    suspend fun getQuote(source: QuoteSource): Quote
}

/** ZenQuotes sends its rate-limit warning as a normal quote with this author. */
private const val RATE_LIMIT_AUTHOR = "zenquotes.io"

/**
 * Loads quotes from the web API and falls back to [fallbackQuotes] when the request fails.
 */
class NetworkQuoteRepository(
    private val api: QuoteApi,
    private val fallbackQuotes: List<Quote> = LocalQuotes.all
) : QuoteRepository {

    override suspend fun getQuote(source: QuoteSource): Quote =
        try {
            val response = when (source) {
                QuoteSource.Today -> api.getQuoteOfTheDay()
                QuoteSource.Random -> api.getRandomQuote()
            }
            response.firstOrNull()
                ?.takeUnless { it.isRateLimitNotice() }
                ?.toQuote()
                ?: offlineQuote(source)
        } catch (e: CancellationException) {
            // Never swallow cancellation, or the ViewModel cannot cancel a stale request.
            throw e
        } catch (e: Exception) {
            // Network, HTTP and JSON problems all degrade to a saved quote.
            offlineQuote(source)
        }

    private fun QuoteDto.isRateLimitNotice(): Boolean =
        author.equals(RATE_LIMIT_AUTHOR, ignoreCase = true)

    /** "Today" picks by day of year so it stays stable all day, even offline. */
    private fun offlineQuote(source: QuoteSource): Quote {
        val quote = when (source) {
            QuoteSource.Today -> fallbackQuotes[LocalDate.now().dayOfYear % fallbackQuotes.size]
            QuoteSource.Random -> fallbackQuotes.random()
        }
        return quote.copy(isOffline = true)
    }
}
