package au.edu.jcu.assessment.utilityapp.ui.theme

import androidx.annotation.StringRes
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import au.edu.jcu.assessment.utilityapp.R

/**
 * The look of the app. Each backdrop carries its gradient and a matching Material 3
 * [ColorScheme], so every component stays readable whichever backdrop is active.
 */
enum class Backdrop(
    @StringRes val labelRes: Int,
    val top: Color,
    val bottom: Color,
    val isDark: Boolean,
    val colorScheme: ColorScheme
) {
    Dawn(
        R.string.backdrop_dawn, DawnTop, DawnBottom, isDark = false,
        colorScheme = backdropScheme(
            isDark = false, base = DawnTop, accent = DawnAccent, onAccent = DawnOnAccent,
            ink = DawnInk, inkMuted = DawnInkMuted, tonal = DawnTonal, onTonal = DawnOnTonal,
            outline = DawnOutline
        )
    ),
    Day(
        R.string.backdrop_day, DayTop, DayBottom, isDark = false,
        colorScheme = backdropScheme(
            isDark = false, base = DayTop, accent = DayAccent, onAccent = DayOnAccent,
            ink = DayInk, inkMuted = DayInkMuted, tonal = DayTonal, onTonal = DayOnTonal,
            outline = DayOutline
        )
    ),
    Dusk(
        R.string.backdrop_dusk, DuskTop, DuskBottom, isDark = true,
        colorScheme = backdropScheme(
            isDark = true, base = DuskTop, accent = DuskAccent, onAccent = DuskOnAccent,
            ink = DuskInk, inkMuted = DuskInkMuted, tonal = DuskTonal, onTonal = DuskOnTonal,
            outline = DuskOutline
        )
    ),
    Night(
        R.string.backdrop_night, NightTop, NightBottom, isDark = true,
        colorScheme = backdropScheme(
            isDark = true, base = NightTop, accent = NightAccent, onAccent = NightOnAccent,
            ink = NightInk, inkMuted = NightInkMuted, tonal = NightTonal, onTonal = NightOnTonal,
            outline = NightOutline
        )
    );

    companion object {
        /** Picks the backdrop that suits [hour] (0-23). */
        fun forHour(hour: Int): Backdrop = when (hour) {
            in 5..8 -> Dawn
            in 9..16 -> Day
            in 17..19 -> Dusk
            else -> Night
        }
    }
}

/** Vertical gradient for a backdrop. Colours can be overridden to animate between backdrops. */
fun Backdrop.verticalBrush(top: Color = this.top, bottom: Color = this.bottom): Brush =
    Brush.verticalGradient(listOf(top, bottom))

private fun backdropScheme(
    isDark: Boolean,
    base: Color,
    accent: Color,
    onAccent: Color,
    ink: Color,
    inkMuted: Color,
    tonal: Color,
    onTonal: Color,
    outline: Color
): ColorScheme =
    if (isDark) {
        darkColorScheme(
            primary = accent, onPrimary = onAccent,
            primaryContainer = tonal, onPrimaryContainer = onTonal,
            secondaryContainer = tonal, onSecondaryContainer = onTonal,
            background = base, onBackground = ink,
            surface = base, onSurface = ink,
            surfaceVariant = tonal, onSurfaceVariant = inkMuted,
            outline = outline, outlineVariant = outline
        )
    } else {
        lightColorScheme(
            primary = accent, onPrimary = onAccent,
            primaryContainer = tonal, onPrimaryContainer = onTonal,
            secondaryContainer = tonal, onSecondaryContainer = onTonal,
            background = base, onBackground = ink,
            surface = base, onSurface = ink,
            surfaceVariant = tonal, onSurfaceVariant = inkMuted,
            outline = outline, outlineVariant = outline
        )
    }
