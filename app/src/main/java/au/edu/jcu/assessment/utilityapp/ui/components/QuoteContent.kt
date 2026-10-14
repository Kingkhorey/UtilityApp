package au.edu.jcu.assessment.utilityapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import au.edu.jcu.assessment.utilityapp.R
import au.edu.jcu.assessment.utilityapp.data.model.Quote
import au.edu.jcu.assessment.utilityapp.ui.theme.QuoteMarkStyle
import au.edu.jcu.assessment.utilityapp.ui.theme.quoteTextStyle

/**
 * The quote itself: a large opening mark, the serif quote text and, optionally, the author.
 * The big quote mark in the accent colour is the one decorative element on the screen.
 */
@Composable
fun QuoteContent(
    quote: Quote,
    showAuthor: Boolean,
    textScale: Float,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        Text(
            text = "“",
            style = QuoteMarkStyle,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Visible,
            modifier = Modifier.height(44.dp)
        )
        Text(
            text = quote.text,
            style = quoteTextStyle(quote.text.length, textScale),
            color = MaterialTheme.colorScheme.onSurface
        )
        AnimatedVisibility(visible = showAuthor) {
            AuthorLine(
                author = quote.author,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
        if (quote.isOffline) {
            OfflineNote(modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
private fun AuthorLine(author: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(width = 28.dp, height = 2.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = author,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun OfflineNote(modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.CloudOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.status_offline),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
