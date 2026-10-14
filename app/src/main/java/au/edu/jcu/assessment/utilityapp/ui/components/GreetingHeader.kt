package au.edu.jcu.assessment.utilityapp.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import au.edu.jcu.assessment.utilityapp.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Greeting and today's date, so the screen answers "what day is it" at a glance. */
@Composable
fun GreetingHeader(
    modifier: Modifier = Modifier,
    hour: Int = LocalTime.now().hour,
    date: LocalDate = LocalDate.now()
) {
    val formattedDate = remember(date) {
        date.format(DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.getDefault()))
    }
    Column(modifier) {
        Text(
            text = stringResource(greetingFor(hour)),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = formattedDate,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@StringRes
private fun greetingFor(hour: Int): Int = when (hour) {
    in 5..11 -> R.string.greeting_morning
    in 12..16 -> R.string.greeting_afternoon
    in 17..21 -> R.string.greeting_evening
    else -> R.string.greeting_night
}
