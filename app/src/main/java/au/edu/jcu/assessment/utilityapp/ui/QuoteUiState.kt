package au.edu.jcu.assessment.utilityapp.ui

import au.edu.jcu.assessment.utilityapp.data.model.Quote

/** What the quote screen should show right now. */
sealed interface QuoteUiState {
    data object Loading : QuoteUiState
    data class Success(val quote: Quote) : QuoteUiState
}
