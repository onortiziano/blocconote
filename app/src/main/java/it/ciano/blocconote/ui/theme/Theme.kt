package it.ciano.blocconote.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

// --- I NOSTRI COLORI PERSONALIZZATI ---
data class AppColors(
    val zebra1: Color,
    val zebra2: Color,
    val border: Color
)

val LocalAppColors = staticCompositionLocalOf { 
    AppColors(Color.White, Color.LightGray, Color.Gray) 
}

private val DarkColorScheme = darkColorScheme()
private val LightColorScheme = lightColorScheme()

@Composable
fun BlocconoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // --- LOGICA ZEBRA ---
    // Questi colori vengono letti direttamente da Color.kt
    val zebra1 = if (darkTheme) DarkZebra1 else LightZebra1
    val zebra2 = if (darkTheme) DarkZebra2 else LightZebra2
    val border = if (darkTheme) DarkBorder else LightBorder

    CompositionLocalProvider(LocalAppColors provides AppColors(zebra1, zebra2, border)) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
