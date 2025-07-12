package com.example.fibra_labeling.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val LightColorScheme = lightColorScheme(

    primary = Color(0XFF26547c),
    secondary = Color(0XFF427aa1),
    tertiary = Color(0xFF3a506b),
    background = Color(0xFF233d4d)

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

val LightColors = lightColorScheme(
    primary = FioriPrimary,
    onPrimary = FioriOnPrimary,
    secondary = FioriSecondary,
    onSecondary = FioriOnSecondary,
    background = FioriBackground,
    onBackground = FioriOnBackground,
    surface = FioriSurface,
    onSurface = FioriOnSurface,
    primaryContainer = FioriPrimaryContainer,
    onPrimaryContainer = FioriOnPrimaryContainer,
)



private val DarkColors = darkColorScheme(
    primary = FioriDarkPrimary,
    onPrimary = FioriDarkOnPrimary,
    secondary = FioriDarkSecondary,
    onSecondary = FioriDarkOnSecondary,
    background = FioriDarkBackground,
    onBackground = FioriDarkOnBackground,
    surface = FioriDarkSurface,
    onSurface = FioriDarkOnSurface,
    outline = FioriDarkOutline
)

val DarkColors2 = darkColorScheme(
    primary = Color(0xFF6BB6FF), // Azul más claro para modo oscuro
    onPrimary = Color.Black,
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    primaryContainer = Color(0xFF003D75),
    onPrimaryContainer = Color.White,
)

@Composable
fun Fibra_labelingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> darkScheme
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}