package it.ciano.blocconote.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

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

@Composable
fun BlocconoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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
  } //  <-PER CASO MANCAVA QUESTA?
	// 1. Scegliamo i colori in base al tema (dentro la funzione)
    val zebra1 = if (darkTheme) DarkZebra1 else LightZebra1
    val zebra2 = if (darkTheme) DarkZebra2 else LightZebra2
    val border = if (darkTheme) DarkBorder else LightBorder
	
	
	// 2. Avvolgiamo il MaterialTheme con il nostro Provider
    CompositionLocalProvider(LocalAppColors provides AppColors(zebra1, zebra2, border)) {

        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
	

// Questa è una piccola "classe" per trasportare i colori
data class AppColors(
    val zebra1: Color,
    val zebra2: Color,
    val border: Color
)

// Questo serve a rendere i colori accessibili in tutta l'app
val LocalAppColors = staticCompositionLocalOf { 
    AppColors(Color.White, Color.LightGray, Color.Gray) 
}
