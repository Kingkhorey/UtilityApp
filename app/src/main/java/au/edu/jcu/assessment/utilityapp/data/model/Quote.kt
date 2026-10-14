package au.edu.jcu.assessment.utilityapp.data.model

/** A quote ready to show on screen. */
data class Quote(
    val text: String,
    val author: String,
    /** True when the quote came from the bundled list because the network was unavailable. */
    val isOffline: Boolean = false
)

/** Plain-text version of a quote, used for copying and sharing. */
fun Quote.asShareText(): String = "“$text”\n— $author"
