package ap.mobile.challenge.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Dark Color Scheme - Vegas Neon Casino Theme
 * High contrast, vibrant neons, premium feel
 */
private val DarkColorScheme = darkColorScheme(
  // Primary colors - Neon green theme
  primary = DarkPrimary,
  onPrimary = DarkOnPrimary,
  primaryContainer = DarkPrimary.copy(alpha = 0.2f),
  onPrimaryContainer = DarkOnBackground,

  // Secondary colors - Hot pink accents
  secondary = DarkSecondary,
  onSecondary = DarkOnSecondary,
  secondaryContainer = DarkSecondary.copy(alpha = 0.2f),
  onSecondaryContainer = DarkOnBackground,

  // Tertiary colors - Cyan highlights
  tertiary = DarkTertiary,
  onTertiary = DarkOnTertiary,
  tertiaryContainer = DarkTertiary.copy(alpha = 0.2f),
  onTertiaryContainer = DarkOnBackground,

  // Background & Surface
  background = DarkBackground,
  onBackground = DarkOnBackground,
  surface = DarkSurface,
  onSurface = DarkOnSurface,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkOnSurface,

  // Surface containers
  surfaceContainer = DarkSurface,
  surfaceContainerHigh = DarkSurfaceVariant,
  surfaceContainerHighest = Color(0xFF2D333B),
  surfaceContainerLow = Color(0xFF161B22),
  surfaceContainerLowest = DarkBackground,

  // Error colors
  error = LoseRed,
  onError = Color.White,
  errorContainer = LoseRed.copy(alpha = 0.2f),
  onErrorContainer = DarkOnBackground,

  // Outline
  outline = Color(0xFF8B949E),
  outlineVariant = Color(0xFF484F58),

  // Surface tint
  surfaceTint = DarkPrimary,

  // Inverse colors
  inverseSurface = LightSurface,
  inverseOnSurface = DarkBackground,
  inversePrimary = LightPrimary,

  // Scrim
  scrim = Color.Black.copy(alpha = 0.6f)
)

/**
 * Light Color Scheme - Candy Pop Casino Theme
 * Fresh, vibrant, playful but professional
 */
private val LightColorScheme = lightColorScheme(
  // Primary colors - Fresh mint theme
  primary = LightPrimary,
  onPrimary = LightOnPrimary,
  primaryContainer = LightPrimary.copy(alpha = 0.15f),
  onPrimaryContainer = LightOnBackground,

  // Secondary colors - Coral pink accents
  secondary = LightSecondary,
  onSecondary = LightOnSecondary,
  secondaryContainer = LightSecondary.copy(alpha = 0.15f),
  onSecondaryContainer = LightOnBackground,

  // Tertiary colors - Ocean blue highlights
  tertiary = LightTertiary,
  onTertiary = LightOnTertiary,
  tertiaryContainer = LightTertiary.copy(alpha = 0.15f),
  onTertiaryContainer = LightOnBackground,

  // Background & Surface
  background = LightBackground,
  onBackground = LightOnBackground,
  surface = LightSurface,
  onSurface = LightOnSurface,
  surfaceVariant = LightSurfaceVariant,
  onSurfaceVariant = LightOnSurface,

  // Surface containers
  surfaceContainer = LightSurface,
  surfaceContainerHigh = Color(0xFFF6F8FA),
  surfaceContainerHighest = Color(0xFFEAEEF2),
  surfaceContainerLow = LightSurface,
  surfaceContainerLowest = Color.White,

  // Error colors
  error = LoseRed,
  onError = Color.White,
  errorContainer = LoseRed.copy(alpha = 0.15f),
  onErrorContainer = LightOnBackground,

  // Outline
  outline = Color(0xFFD0D7DE),
  outlineVariant = Color(0xFFE8EAED),

  // Surface tint
  surfaceTint = LightPrimary,

  // Inverse colors
  inverseSurface = DarkSurface,
  inverseOnSurface = LightBackground,
  inversePrimary = DarkPrimary,

  // Scrim
  scrim = Color.Black.copy(alpha = 0.5f)
)

/**
 * Challenge Theme - Main theme wrapper
 */
@Composable
fun ChallengeTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context)
      else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = if (darkTheme) { // <-- Baris yang tidak digunakan
        DarkBackground.toArgb()
      } else {
        LightBackground.toArgb()
      }
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}