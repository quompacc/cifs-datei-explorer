package com.wa2c.android.cifsdocumentsprovider.presentation.ui.common

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

object Theme {

    /** Fallback palette (Android < 12 or dynamic color unavailable) */
    private val LightColors = lightColorScheme(
        primary = Color(0xFF415F91),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFD6E3FF),
        onPrimaryContainer = Color(0xFF284777),
        secondary = Color(0xFF565F71),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFDAE2F9),
        onSecondaryContainer = Color(0xFF3E4759),
        tertiary = Color(0xFF705575),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFAD8FD),
        onTertiaryContainer = Color(0xFF573E5C),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF93000A),
        background = Color(0xFFF9F9FF),
        onBackground = Color(0xFF191C20),
        surface = Color(0xFFF9F9FF),
        onSurface = Color(0xFF191C20),
        surfaceVariant = Color(0xFFE0E2EC),
        onSurfaceVariant = Color(0xFF44474E),
        outline = Color(0xFF74777F),
        outlineVariant = Color(0xFFC4C6D0),
        inverseSurface = Color(0xFF2E3036),
        inverseOnSurface = Color(0xFFF0F0F7),
        inversePrimary = Color(0xFFAAC7FF),
        surfaceDim = Color(0xFFD9D9E0),
        surfaceBright = Color(0xFFF9F9FF),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF3F3FA),
        surfaceContainer = Color(0xFFEDEDF4),
        surfaceContainerHigh = Color(0xFFE7E8EE),
        surfaceContainerHighest = Color(0xFFE2E2E9),
    )

    private val DarkColors = darkColorScheme(
        primary = Color(0xFFAAC7FF),
        onPrimary = Color(0xFF0A305F),
        primaryContainer = Color(0xFF284777),
        onPrimaryContainer = Color(0xFFD6E3FF),
        secondary = Color(0xFFBEC6DC),
        onSecondary = Color(0xFF283141),
        secondaryContainer = Color(0xFF3E4759),
        onSecondaryContainer = Color(0xFFDAE2F9),
        tertiary = Color(0xFFDDBCE0),
        onTertiary = Color(0xFF3F2844),
        tertiaryContainer = Color(0xFF573E5C),
        onTertiaryContainer = Color(0xFFFAD8FD),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF111318),
        onBackground = Color(0xFFE2E2E9),
        surface = Color(0xFF111318),
        onSurface = Color(0xFFE2E2E9),
        surfaceVariant = Color(0xFF44474E),
        onSurfaceVariant = Color(0xFFC4C6D0),
        outline = Color(0xFF8E9099),
        outlineVariant = Color(0xFF44474E),
        inverseSurface = Color(0xFFE2E2E9),
        inverseOnSurface = Color(0xFF2E3036),
        inversePrimary = Color(0xFF415F91),
        surfaceDim = Color(0xFF111318),
        surfaceBright = Color(0xFF37393E),
        surfaceContainerLowest = Color(0xFF0C0E13),
        surfaceContainerLow = Color(0xFF191C20),
        surfaceContainer = Color(0xFF1D2024),
        surfaceContainerHigh = Color(0xFF282A2F),
        surfaceContainerHighest = Color(0xFF33353A),
    )

    object Colors {
        /** Uncheck */
        val CheckUc = Color(0xFF8E9099)
        /** Check result OK */
        val CheckOk = Color(0xFF2E9D5B)
        /** Check result Warning */
        val CheckWn = Color(0xFFD69E1E)
        /** Check result Ng */
        val CheckNg = Color(0xFFD93B3B)

        /** Loading backgrouind */
        val LoadingBackground = Color(0x66000000)
    }

    object Sizes {
        val SS = 4.dp
        val S = 8.dp
        val M = 16.dp
        val L = 24.dp
        val LL = 32.dp

        val ScreenMargin = M
        val Button = 48.dp
    }

    private val AppShapes = Shapes(
        extraSmall = RoundedCornerShape(8.dp),
        small = RoundedCornerShape(12.dp),
        medium = RoundedCornerShape(16.dp),
        large = RoundedCornerShape(24.dp),
        extraLarge = RoundedCornerShape(32.dp),
    )

    private val AppTypography = Typography().let {
        it.copy(
            headlineMedium = it.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
            headlineSmall = it.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
            titleLarge = it.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            titleMedium = it.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            titleSmall = it.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            labelLarge = it.labelLarge.copy(fontWeight = FontWeight.SemiBold),
        )
    }

    @Composable
    fun AppTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        dynamicColor: Boolean = true,
        content: @Composable () -> Unit
    ) {
        val colors = when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }
            darkTheme -> DarkColors
            else -> LightColors
        }

        MaterialTheme(
            colorScheme = colors,
            typography = AppTypography,
            shapes = AppShapes,
        ) {
            Surface(content = content)
        }
    }

}
