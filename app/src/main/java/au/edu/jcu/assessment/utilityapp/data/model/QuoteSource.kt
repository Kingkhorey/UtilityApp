package au.edu.jcu.assessment.utilityapp.data.model

/** Where the next quote should come from. */
enum class QuoteSource {
    /** The same quote all day. */
    Today,

    /** A different quote every time. */
    Random
}
