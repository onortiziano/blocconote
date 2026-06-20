package it.ciano.blocconote.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
// Importiamo i colori definiti in Color.kt
import it.ciano.blocconote.ui.theme.Purple80
import it.ciano.blocconote.ui.theme.PurpleGrey80
import it.ciano.blocconote.ui.theme.Pink80
import it.ciano.blocconote.ui.theme.Purple40
import it.ciano.blocconote.ui.theme.PurpleGrey40
import it.ciano.blocconote.ui.theme.Pink40

// --- I NOSTRI COLORI PERSONALIZZATI ---
data class AppColors(
    val zebra1: Color,
    val zebra2: Color,
    val border: Color
)

val LocalAppColors = staticCompositionLocalOf { 
    AppColors(Color.White, Color.LightGray, Color.Gray) 
}

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

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

    // Logica per la Zebra
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