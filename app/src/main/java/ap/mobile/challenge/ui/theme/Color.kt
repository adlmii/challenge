package ap.mobile.challenge.ui.theme

import androidx.compose.ui.graphics.Color

// ========== Dark Theme - Vegas Neon Casino 🎰✨ ==========
val DarkPrimary = Color(0xFF00FFA3)        // Electric Neon Green - Pops like crazy
val DarkSecondary = Color(0xFFFF006E)      // Hot Pink - Energetic accent
val DarkTertiary = Color(0xFF00D9FF)       // Cyan Blue - Cool contrast
val DarkBackground = Color(0xFF0D1117)     // Almost Black - Deep casino
val DarkSurface = Color(0xFF161B22)        // Dark Card - Elevated
val DarkSurfaceVariant = Color(0xFF21262D) // Darker Cards - Slot machine

// Dark theme text colors
val DarkOnPrimary = Color(0xFF000000)
val DarkOnSecondary = Color(0xFFFFFFFF)
val DarkOnTertiary = Color(0xFF000000)
val DarkOnBackground = Color(0xFFFFFFFF)
val DarkOnSurface = Color(0xFFE6EDF3)

// ========== Light Theme - Candy Pop Casino 🍬🎨 ==========
val LightPrimary = Color(0xFF00D084)       // Fresh Mint - Vibrant but not harsh
val LightSecondary = Color(0xFFFF3D71)     // Coral Pink - Playful
val LightTertiary = Color(0xFF00B8D4)      // Ocean Blue - Calm accent
val LightBackground = Color(0xFFFAFBFC)    // Pure White - Clean
val LightSurface = Color(0xFFFFFFFF)       // White Surface - Crisp
val LightSurfaceVariant = Color(0xFFF6F8FA) // Light Gray - Subtle depth

// Light theme text colors
val LightOnPrimary = Color(0xFFFFFFFF)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightOnTertiary = Color(0xFFFFFFFF)
val LightOnBackground = Color(0xFF24292F)
val LightOnSurface = Color(0xFF24292F)

// ========== Accent Colors - Eye-Popping! 💥 ==========
val WinGreen = Color(0xFF00FF88)           // Neon Green Win - Ultra bright!
val LoseRed = Color(0xFFFF0055)            // Hot Red - Intense
val SlotGold = Color(0xFFFFD700)           // Pure Gold - Luxury
val BonusPurple = Color(0xFFBF40BF)        // Electric Purple - Special

// ========== Gradient Colors - Premium Depth 🌈 ==========
val GradientGreen1 = Color(0xFF00FFA3)
val GradientGreen2 = Color(0xFF00D084)
val GradientGreen3 = Color(0xFF00B875)

val GradientPink1 = Color(0xFFFF006E)
val GradientPink2 = Color(0xFFFF3D71)
val GradientPink3 = Color(0xFFFF5E89)

val GradientBlue1 = Color(0xFF00D9FF)
val GradientBlue2 = Color(0xFF00B8D4)
val GradientBlue3 = Color(0xFF0096C7)

val GradientGold1 = Color(0xFFFFD700)
val GradientGold2 = Color(0xFFFFC700)
val GradientGold3 = Color(0xFFFFB800)

// ========== Semantic Colors 🎯 ==========
val SuccessGreen = Color(0xFF10B981)
val WarningYellow = Color(0xFFFFBB00)      // Brighter warning
val ErrorRed = Color(0xFFFF0055)           // Match lose red
val InfoBlue = Color(0xFF00B8D4)

// ========== Component Specific Colors ==========
object SlotColors {
    // Active slot (stopped)
    val ActiveBorder = SlotGold
    val ActiveGlow = SlotGold.copy(alpha = 0.4f)
    val ActiveBackground = Color(0xFF2D1F00).copy(alpha = 0.3f) // Dark gold tint

    // Rolling slot
    val RollingBorder = Color(0xFF8B949E)
    val RollingBackground = Color.Transparent

    // Slot backgrounds by state
    val WinningSlotGlow = WinGreen.copy(alpha = 0.3f)
    val LosingSlotGlow = Color.Transparent
}

object HistoryColors {
    val WinBackground = WinGreen.copy(alpha = 0.08f)
    val WinBorder = WinGreen.copy(alpha = 0.3f)
    val WinBadge = WinGreen.copy(alpha = 0.15f)

    val LoseBackground = LoseRed.copy(alpha = 0.05f)
    val LoseBorder = LoseRed.copy(alpha = 0.2f)
    val LoseBadge = LoseRed.copy(alpha = 0.12f)

    val StatusDotWin = WinGreen
    val StatusDotLose = LoseRed
}

object ButtonColors {
    val PrimaryEnabled = Color(0xFF00FFA3)
    val PrimaryPressed = Color(0xFF00D084)
    val PrimaryHover = Color(0xFF00FFB8)
    val PrimaryDisabled = Color(0xFF6E7681)

    val SecondaryEnabled = Color(0xFFFF006E)
    val SecondaryPressed = Color(0xFFFF3D71)

    val OutlineDefault = Color(0xFF8B949E)
}

object ShadowColors {
    val Primary = Color(0xFF00FFA3).copy(alpha = 0.3f)
    val Secondary = Color(0xFFFF006E).copy(alpha = 0.3f)
    val Tertiary = Color(0xFF00D9FF).copy(alpha = 0.3f)
    val Gold = SlotGold.copy(alpha = 0.4f)
    val Default = Color(0xFF000000).copy(alpha = 0.25f)
}

// ========== Legacy Colors ==========
val Purple80 = Color(0xFF00FFA3)
val PurpleGrey80 = Color(0xFF00D9FF)
val Pink80 = Color(0xFFFF006E)

val Purple40 = Color(0xFF00D084)
val PurpleGrey40 = Color(0xFF00B8D4)
val Pink40 = Color(0xFFFF3D71)