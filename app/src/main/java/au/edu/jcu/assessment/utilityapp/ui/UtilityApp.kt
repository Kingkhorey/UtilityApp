package au.edu.jcu.assessment.utilityapp.ui

import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.jcu.assessment.utilityapp.R
import au.edu.jcu.assessment.utilityapp.data.model.Quote
import au.edu.jcu.assessment.utilityapp.data.model.QuoteSource
import au.edu.jcu.assessment.utilityapp.data.model.asShareText
import au.edu.jcu.assessment.utilityapp.ui.theme.Backdrop
import au.edu.jcu.assessment.utilityapp.ui.theme.DailyQuotesTheme
import kotlinx.coroutines.launch
import java.time.LocalTime

private const val BACKDROP_FADE_MILLIS = 600

/** The two bottom-bar destinations. */
private enum class UtilityTab(@StringRes val labelRes: Int, val icon: ImageVector) {
    Daily(R.string.tab_quote, Icons.Default.FormatQuote),
    Settings(R.string.tab_settings, Icons.Default.Settings)
}

/**
 * App entry point. Connects the ViewModel to the UI and handles the two actions that need
 * Android services (clipboard and share sheet). Everything visual is in [UtilityAppContent].
 */
@Composable
fun UtilityApp(viewModel: QuoteViewModel = viewModel(factory = QuoteViewModel.Factory)) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val backdrop = settings.backdropChoice ?: Backdrop.forHour(LocalTime.now().hour)

    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val copiedMessage = stringResource(R.string.quote_copied)
    val shareTitle = stringResource(R.string.share_title)

    DailyQuotesTheme(backdrop = backdrop) {
        UtilityAppContent(
            uiState = uiState,
            settings = settings,
            backdrop = backdrop,
            snackbarHostState = snackbarHostState,
            onNewQuote = viewModel::onNewQuote,
            onCopy = {
                (uiState as? QuoteUiState.Success)?.let { state ->
                    clipboard.setText(AnnotatedString(state.quote.asShareText()))
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(copiedMessage)
                    }
                }
            },
            onShare = {
                (uiState as? QuoteUiState.Success)?.let { state ->
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, state.quote.asShareText())
                    }
                    context.startActivity(Intent.createChooser(sendIntent, shareTitle))
                }
            },
            onSourceChange = viewModel::onSourceChange,
            onShowAuthorChange = viewModel::onShowAuthorChange,
            onTextScaleChange = viewModel::onTextScaleChange,
            onBackdropChange = viewModel::onBackdropChange
        )
    }
}

/**
 * Scaffold with the gradient backdrop and bottom navigation. It has no ViewModel, so it
 * can be previewed with fake state.
 */
@Composable
fun UtilityAppContent(
    uiState: QuoteUiState,
    settings: UtilitySettings,
    backdrop: Backdrop,
    snackbarHostState: SnackbarHostState,
    onNewQuote: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onSourceChange: (QuoteSource) -> Unit,
    onShowAuthorChange: (Boolean) -> Unit,
    onTextScaleChange: (Float) -> Unit,
    onBackdropChange: (Backdrop?) -> Unit
) {
    // rememberSaveable keeps the selected tab when the screen rotates.
    var selectedTab by rememberSaveable { mutableStateOf(UtilityTab.Daily) }

    val top by animateColorAsState(backdrop.top, tween(BACKDROP_FADE_MILLIS), label = "backdropTop")
    val bottom by animateColorAsState(backdrop.bottom, tween(BACKDROP_FADE_MILLIS), label = "backdropBottom")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(top, bottom)))
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                NavigationBar(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    tonalElevation = 0.dp
                ) {
                    UtilityTab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            icon = { Icon(tab.icon, contentDescription = null) },
                            label = { Text(stringResource(tab.labelRes)) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            val contentModifier = Modifier.padding(innerPadding)
            when (selectedTab) {
                UtilityTab.Daily -> UtilityScreen(
                    uiState = uiState,
                    showAuthor = settings.showAuthor,
                    textScale = settings.textScale,
                    onNewQuote = onNewQuote,
                    onCopy = onCopy,
                    onShare = onShare,
                    modifier = contentModifier
                )

                UtilityTab.Settings -> SettingsScreen(
                    settings = settings,
                    onSourceChange = onSourceChange,
                    onShowAuthorChange = onShowAuthorChange,
                    onTextScaleChange = onTextScaleChange,
                    onBackdropChange = onBackdropChange,
                    modifier = contentModifier
                )
            }
        }
    }
}

// ---------- Previews ----------

private val previewQuote = Quote(
    text = "We suffer more often in imagination than in reality.",
    author = "Seneca"
)

@Composable
private fun PreviewApp(backdrop: Backdrop, uiState: QuoteUiState) {
    DailyQuotesTheme(backdrop = backdrop) {
        UtilityAppContent(
            uiState = uiState,
            settings = UtilitySettings(backdropChoice = backdrop),
            backdrop = backdrop,
            snackbarHostState = remember { SnackbarHostState() },
            onNewQuote = {},
            onCopy = {},
            onShare = {},
            onSourceChange = {},
            onShowAuthorChange = {},
            onTextScaleChange = {},
            onBackdropChange = {}
        )
    }
}

@Preview(name = "Dawn", showSystemUi = true)
@Composable
private fun DawnPreview() = PreviewApp(Backdrop.Dawn, QuoteUiState.Success(previewQuote))

@Preview(name = "Day", showSystemUi = true)
@Composable
private fun DayPreview() = PreviewApp(Backdrop.Day, QuoteUiState.Success(previewQuote))

@Preview(name = "Dusk", showSystemUi = true)
@Composable
private fun DuskPreview() = PreviewApp(Backdrop.Dusk, QuoteUiState.Success(previewQuote))

@Preview(name = "Night (offline)", showSystemUi = true)
@Composable
private fun NightOfflinePreview() =
    PreviewApp(Backdrop.Night, QuoteUiState.Success(previewQuote.copy(isOffline = true)))

@Preview(name = "Loading", showSystemUi = true)
@Composable
private fun LoadingPreview() = PreviewApp(Backdrop.Day, QuoteUiState.Loading)
