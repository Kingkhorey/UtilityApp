package au.edu.jcu.assessment.utilityapp.data.local

import au.edu.jcu.assessment.utilityapp.data.model.Quote

/** Public-domain quotes shown when the web API cannot be reached. */
object LocalQuotes {
    val all: List<Quote> = listOf(
        Quote("We suffer more often in imagination than in reality.", "Seneca"),
        Quote("It is not that we have a short time to live, but that we waste a lot of it.", "Seneca"),
        Quote("Begin at once to live, and count each separate day as a separate life.", "Seneca"),
        Quote("A journey of a thousand miles begins with a single step.", "Lao Tzu"),
        Quote("The unexamined life is not worth living.", "Socrates"),
        Quote("Well done is better than well said.", "Benjamin Franklin"),
        Quote("Nothing great was ever achieved without enthusiasm.", "Ralph Waldo Emerson"),
        Quote("He who has a why to live can bear almost any how.", "Friedrich Nietzsche"),
        Quote("Fall seven times, stand up eight.", "Japanese proverb"),
        Quote("It's not what you look at that matters, it's what you see.", "Henry David Thoreau")
    )
}
