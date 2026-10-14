package au.edu.jcu.assessment.utilityapp.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import au.edu.jcu.assessment.utilityapp.R
import au.edu.jcu.assessment.utilityapp.data.model.QuoteSource
import au.edu.jcu.assessment.utilityapp.ui.components.ChoiceChips
import au.edu.jcu.assessment.utilityapp.ui.components.SettingsSection
import au.edu.jcu.assessment.utilityapp.ui.theme.Backdrop

private const val MIN_TEXT_SCALE = 0.8f
private const val MAX_TEXT_SCALE = 1.4f

// 0.8, 0.9 ... 1.4 gives six positions, which is five steps between the ends.
private const val TEXT_SCALE_STEPS = 5

/**
 * Settings screen. Every control here changes something on the quote screen straight away.
 */
@Composable
fun SettingsScreen(
    settings: UtilitySettings,
    onSourceChange: (QuoteSource) -> Unit,
    onShowAuthorChange: (Boolean) -> Unit,
    onTextScaleChange: (Float) -> Unit,
    onBackdropChange: (Backdrop?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.settings_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        SettingsSection(
            title = stringResource(R.string.settings_source_title),
            description = stringResource(R.string.settings_source_description)
        ) {
            ChoiceChips(
                options = QuoteSource.entries,
                selected = settings.source,
                label = { stringResource(sourceLabel(it)) },
                onSelect = onSourceChange
            )
        }

        SectionDivider()

        ShowAuthorRow(checked = settings.showAuthor, onCheckedChange = onShowAuthorChange)

        SectionDivider()

        TextSizeSection(scale = settings.textScale, onScaleChange = onTextScaleChange)

        SectionDivider()

        SettingsSection(
            title = stringResource(R.string.settings_backdrop_title),
            description = stringResource(R.string.settings_backdrop_description)
        ) {
            // Null stands for "Auto" (follow the time of day).
            ChoiceChips(
                options = listOf<Backdrop?>(null) + Backdrop.entries,
                selected = settings.backdropChoice,
                label = { stringResource(it?.labelRes ?: R.string.backdrop_auto) },
                onSelect = onBackdropChange
            )
        }

        Text(
            text = stringResource(R.string.settings_attribution),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
}

/** The whole row is the touch target, so it is easy to hit and works with screen readers. */
@Composable
private fun ShowAuthorRow(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(value = checked, role = Role.Switch, onValueChange = onCheckedChange),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.settings_author_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Switch(checked = checked, onCheckedChange = null)
    }
}

@Composable
private fun TextSizeSection(scale: Float, onScaleChange: (Float) -> Unit) {
    SettingsSection(title = stringResource(R.string.settings_text_size_title)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "A",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Slider(
                value = scale,
                onValueChange = onScaleChange,
                valueRange = MIN_TEXT_SCALE..MAX_TEXT_SCALE,
                steps = TEXT_SCALE_STEPS,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            )
            Text(
                text = "A",
                fontSize = 26.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@StringRes
private fun sourceLabel(source: QuoteSource): Int = when (source) {
    QuoteSource.Today -> R.string.source_today
    QuoteSource.Random -> R.string.source_random
}
