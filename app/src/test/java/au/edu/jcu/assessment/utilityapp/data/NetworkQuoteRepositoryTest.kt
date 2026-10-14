package au.edu.jcu.assessment.utilityapp.data

import au.edu.jcu.assessment.utilityapp.data.model.Quote
import au.edu.jcu.assessment.utilityapp.data.model.QuoteSource
import au.edu.jcu.assessment.utilityapp.data.remote.QuoteApi
import au.edu.jcu.assessment.utilityapp.data.remote.QuoteDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class NetworkQuoteRepositoryTest {

    private val savedQuote = Quote(text = "Saved quote", author = "Someone")

    /** Stand-in for the web API so the tests never touch the network. */
    private class FakeQuoteApi(
        private val response: List<QuoteDto> = emptyList(),
        private val error: Exception? = null
    ) : QuoteApi {
        override suspend fun getQuoteOfTheDay(): List<QuoteDto> = result()
        override suspend fun getRandomQuote(): List<QuoteDto> = result()

        private fun result(): List<QuoteDto> {
            error?.let { throw it }
            return response
        }
    }

    private fun repository(api: QuoteApi) = NetworkQuoteRepository(api, listOf(savedQuote))

    @Test
    fun returnsRemoteQuoteWhenRequestSucceeds() = runTest {
        val api = FakeQuoteApi(listOf(QuoteDto(text = "  Hello  ", author = "Ada")))

        val quote = repository(api).getQuote(QuoteSource.Random)

        assertEquals("Hello", quote.text)
        assertEquals("Ada", quote.author)
        assertFalse(quote.isOffline)
    }

    @Test
    fun fallsBackToSavedQuoteWhenNetworkFails() = runTest {
        val api = FakeQuoteApi(error = IOException("no connection"))

        val quote = repository(api).getQuote(QuoteSource.Today)

        assertEquals(savedQuote.text, quote.text)
        assertTrue(quote.isOffline)
    }

    @Test
    fun fallsBackWhenApiSendsRateLimitNotice() = runTest {
        val api = FakeQuoteApi(listOf(QuoteDto(text = "Too many requests.", author = "zenquotes.io")))

        val quote = repository(api).getQuote(QuoteSource.Random)

        assertEquals(savedQuote.text, quote.text)
        assertTrue(quote.isOffline)
    }

    @Test
    fun fallsBackWhenResponseIsEmpty() = runTest {
        val quote = repository(FakeQuoteApi()).getQuote(QuoteSource.Random)

        assertTrue(quote.isOffline)
    }

    @Test
    fun missingAuthorBecomesUnknown() = runTest {
        val api = FakeQuoteApi(listOf(QuoteDto(text = "No author here", author = null)))

        val quote = repository(api).getQuote(QuoteSource.Random)

        assertEquals("Unknown", quote.author)
    }
}
