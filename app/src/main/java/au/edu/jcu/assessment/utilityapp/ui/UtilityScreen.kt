package au.edu.jcu.assessment.utilityapp.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import au.edu.jcu.assessment.utilityapp.R
import au.edu.jcu.assessment.utilityapp.ui.components.GreetingHeader
import au.edu.jcu.assessment.utilityapp.ui.components.QuoteContent

/**
 * Main screen. Shows one quote and three actions: copy, share and get a new quote.
 * It only displays state and reports taps; the ViewModel decides what happens next.
 */
@Composable
fun UtilityScreen(
    uiState: QuoteUiState,
    showAuthor: Boolean,
    textScale: Float,
    onNewQuote: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 20.dp)
    ) {
        GreetingHeader()
        Crossfade(
            targetState = uiState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            label = "quote"
        ) { state ->
            when (state) {
                QuoteUiState.Loading -> LoadingIndicator()
                is QuoteUiState.Success -> QuoteBody(state, showAuthor, textScale)
            }
        }
        QuoteActions(
            actionsEnabled = uiState is QuoteUiState.Success,
            onNewQuote = onNewQuote,
            onCopy = onCopy,
            onShare = onShare
        )
    }
}

/** Centres the quote when it is short and lets it scroll when it is long. */
@Composable
private fun QuoteBody(
    state: QuoteUiState.Success,
    showAuthor: Boolean,
    textScale: Float
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        QuoteContent(
            quote = state.quote,
            showAuthor = showAuthor,
            textScale = textScale,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = maxHeight)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun LoadingIndicator() {
    val description = stringResource(R.string.loading_quote)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun QuoteActions(
    actionsEnabled: Boolean,
    onNewQuote: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledTonalIconButton(onClick = onCopy, enabled = actionsEnabled) {
                Icon(Icons.Default.ContentCopy, contentDescription = stringResource(R.string.action_copy))
            }
            FilledTonalIconButton(onClick = onShare, enabled = actionsEnabled) {
                Icon(Icons.Default.Share, contentDescription = stringResource(R.string.action_share))
            }
        }
        Button(onClick = onNewQuote, modifier = Modifier.height(52.dp)) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Text(stringResource(R.string.action_new_quote))
        }
    }
}
