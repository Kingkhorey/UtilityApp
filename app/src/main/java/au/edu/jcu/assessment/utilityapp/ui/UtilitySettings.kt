package au.edu.jcu.assessment.utilityapp.ui

import au.edu.jcu.assessment.utilityapp.data.model.QuoteSource
import au.edu.jcu.assessment.utilityapp.ui.theme.Backdrop

/** User choices from the settings screen. Each one changes the quote screen. */
data class UtilitySettings(
    val source: QuoteSource = QuoteSource.Today,
    val showAuthor: Boolean = true,
    /** Multiplier applied to the quote text size. */
    val textScale: Float = 1f,
    /** Null means "follow the time of day". */
    val backdropChoice: Backdrop? = null
)
