package au.edu.jcu.assessment.utilityapp.data.remote

import au.edu.jcu.assessment.utilityapp.data.model.Quote
import com.google.gson.annotations.SerializedName

private const val UNKNOWN_AUTHOR = "Unknown"

/**
 * One item of the ZenQuotes JSON array, e.g. `{"q": "...", "a": "...", "h": "..."}`.
 * Fields are nullable because Gson does not enforce Kotlin null-safety.
 */
data class QuoteDto(
    @SerializedName("q") val text: String?,
    @SerializedName("a") val author: String?
)

/** Converts the network model to the app's [Quote], or null if the quote text is missing. */
fun QuoteDto.toQuote(): Quote? {
    val cleanText = text?.trim().orEmpty()
    if (cleanText.isEmpty()) return null
    return Quote(
        text = cleanText,
        author = author?.trim().orEmpty().ifEmpty { UNKNOWN_AUTHOR }
    )
}
