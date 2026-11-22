package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val BrutalistColorScheme = lightColorScheme(
    primary = OrangeAccent,
    onPrimary = SurfaceWhite,
    primaryContainer = OrangeSoftBg,
    onPrimaryContainer = OrangeAccent,
    secondary = InkBlack,
    onSecondary = SurfaceWhite,
    secondaryContainer = CodeSurface,
    onSecondaryContainer = InkCharcoal,
    tertiary = OrangeBright,
    onTertiary = InkBlack,
    background = SurfaceOffWhite,
    onBackground = InkCharcoal,
    surface = SurfaceWhite,
    onSurface = InkCharcoal,
    surfaceVariant = CodeSurface,
    onSurfaceVariant = SlateMuted,
    outline = InkBlack,
    outlineVariant = SubtleBorder,
    error = ErrorRed,
    onError = SurfaceWhite,
    errorContainer = ErrorBg,
    onErrorContainer = ErrorRed
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BrutalistColorScheme,
        typography = Typography,
        content = content
    )
}
