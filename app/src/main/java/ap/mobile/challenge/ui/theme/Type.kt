package ap.mobile.challenge.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Ultra Modern Typography - Bold, Expressive, Eye-Catching
 */
val Typography = Typography(
  // ========== Display - MEGA TITLES ==========
  displayLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Black,
    fontSize = 64.sp,
    lineHeight = 72.sp,
    letterSpacing = 0.sp
  ),
  displayMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Black,
    fontSize = 52.sp,
    lineHeight = 60.sp,
    letterSpacing = 0.sp
  ),
  displaySmall = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 40.sp,
    lineHeight = 48.sp,
    letterSpacing = 0.sp
  ),

  // ========== Headlines - SECTION HEADERS ==========
  headlineLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 36.sp,
    lineHeight = 44.sp,
    letterSpacing = 0.sp
  ),
  headlineMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    lineHeight = 40.sp,
    letterSpacing = 0.sp
  ),
  headlineSmall = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp,
    lineHeight = 36.sp,
    letterSpacing = 0.sp
  ),

  // ========== Titles - CARD HEADERS ==========
  titleLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    lineHeight = 32.sp,
    letterSpacing = 0.sp
  ),
  titleMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.SemiBold,
    fontSize = 18.sp,
    lineHeight = 26.sp,
    letterSpacing = 0.15.sp
  ),
  titleSmall = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.1.sp
  ),

  // ========== Body - CONTENT TEXT ==========
  bodyLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 18.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.5.sp
  ),
  bodyMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.25.sp
  ),
  bodySmall = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.4.sp
  ),

  // ========== Labels - BUTTONS & BADGES ==========
  labelLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp
  ),
  labelMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.5.sp
  ),
  labelSmall = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
  )
)

/**
 * Custom Typography for Slot Game - Ultra Eye-Catching!
 */
object CustomTypography {
  // ========== GAME TITLES - MASSIVE & BOLD ==========
  val gameTitleMain = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Black,
    fontSize = 56.sp,
    lineHeight = 64.sp,
    letterSpacing = 4.sp  // Wide spacing for impact
  )

  val gameTitleSub = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Black,
    fontSize = 48.sp,
    lineHeight = 56.sp,
    letterSpacing = 3.sp
  )

  val gameTagline = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 1.sp
  )

  // ========== BUTTONS - STRONG & CLEAR ==========
  val buttonHero = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Black,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = 1.5.sp
  )

  val buttonLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 24.sp,
    letterSpacing = 1.sp
  )

  val buttonMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    lineHeight = 22.sp,
    letterSpacing = 0.5.sp
  )

  val buttonSmall = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.5.sp
  )

  // ========== RESULTS - DRAMATIC & BOLD ==========
  val resultHuge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Black,
    fontSize = 40.sp,
    lineHeight = 48.sp,
    letterSpacing = 3.sp
  )

  val resultLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Black,
    fontSize = 32.sp,
    lineHeight = 40.sp,
    letterSpacing = 2.sp
  )

  val resultMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 24.sp,
    lineHeight = 32.sp,
    letterSpacing = 1.sp
  )

  val resultSubtext = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp
  )

  // ========== BADGES & CHIPS - COMPACT & BOLD ==========
  val badgeLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Black,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 1.2.sp
  )

  val badgeMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 1.sp
  )

  val badgeSmall = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.SemiBold,
    fontSize = 10.sp,
    lineHeight = 14.sp,
    letterSpacing = 0.8.sp
  )

  // ========== SECTIONS - CLEAR HIERARCHY ==========
  val sectionHeader = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 24.sp,
    lineHeight = 32.sp,
    letterSpacing = 1.sp
  )

  val sectionSubheader = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.SemiBold,
    fontSize = 18.sp,
    lineHeight = 26.sp,
    letterSpacing = 0.5.sp
  )

  // ========== CAPTION & HELPER TEXT ==========
  val captionLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.4.sp
  )

  val captionMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 13.sp,
    lineHeight = 18.sp,
    letterSpacing = 0.4.sp
  )

  val captionSmall = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.4.sp
  )

  // ========== NUMBERS & STATS - MONOSPACE FEEL ==========
  val numberHuge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Black,
    fontSize = 72.sp,
    lineHeight = 80.sp,
    letterSpacing = (-1).sp
  )

  val numberLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 48.sp,
    lineHeight = 56.sp,
    letterSpacing = 0.sp
  )

  val numberMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.SemiBold,
    fontSize = 32.sp,
    lineHeight = 40.sp,
    letterSpacing = 0.sp
  )
}

/**
 * Typography Helper - Quick Access
 */
object TypographyHelper {
  fun getGameState(isWin: Boolean, size: String = "large"): TextStyle {
    return when (size) {
      "huge" -> CustomTypography.resultHuge
      "large" -> CustomTypography.resultLarge
      "medium" -> CustomTypography.resultMedium
      else -> CustomTypography.resultLarge
    }
  }

  fun getButton(size: String = "large"): TextStyle {
    return when (size) {
      "hero" -> CustomTypography.buttonHero
      "large" -> CustomTypography.buttonLarge
      "medium" -> CustomTypography.buttonMedium
      "small" -> CustomTypography.buttonSmall
      else -> CustomTypography.buttonLarge
    }
  }

  fun getBadge(size: String = "medium"): TextStyle {
    return when (size) {
      "large" -> CustomTypography.badgeLarge
      "medium" -> CustomTypography.badgeMedium
      "small" -> CustomTypography.badgeSmall
      else -> CustomTypography.badgeMedium
    }
  }
}