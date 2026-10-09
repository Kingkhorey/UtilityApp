package au.edu.jcu.assessment.utilityapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import au.edu.jcu.assessment.utilityapp.QuoteApplication
import au.edu.jcu.assessment.utilityapp.data.QuoteRepository
import au.edu.jcu.assessment.utilityapp.data.model.QuoteSource
import au.edu.jcu.assessment.utilityapp.ui.theme.Backdrop
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Holds the quote screen state and the settings, and survives screen rotation.
 * Both screens share this one ViewModel so a settings change shows up on the quote screen.
 */
class QuoteViewModel(private val repository: QuoteRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<QuoteUiState>(QuoteUiState.Loading)
    val uiState: StateFlow<QuoteUiState> = _uiState.asStateFlow()

    private val _settings = MutableStateFlow(UtilitySettings())
    val settings: StateFlow<UtilitySettings> = _settings.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadQuote(_settings.value.source)
    }

    /** Fetches a fresh random quote. */
    fun onNewQuote() = loadQuote(QuoteSource.Random)

    fun onSourceChange(source: QuoteSource) {
        _settings.update { it.copy(source = source) }
        loadQuote(source)
    }

    fun onShowAuthorChange(show: Boolean) {
        _settings.update { it.copy(showAuthor = show) }
    }

    fun onTextScaleChange(scale: Float) {
        _settings.update { it.copy(textScale = scale) }
    }

    fun onBackdropChange(choice: Backdrop?) {
        _settings.update { it.copy(backdropChoice = choice) }
    }

    /** Cancels any request still running so an old answer can never overwrite a newer one. */
    private fun loadQuote(source: QuoteSource) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = QuoteUiState.Loading
            _uiState.value = QuoteUiState.Success(repository.getQuote(source))
        }
    }

    companion object {
        /** Builds the ViewModel with the repository from the application's [AppContainer]. */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as QuoteApplication
                QuoteViewModel(app.container.quoteRepository)
            }
        }
    }
}
