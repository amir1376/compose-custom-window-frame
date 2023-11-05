package ir.amirab

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors

val themes = mapOf(
    Themes.Dark to darkColors(),
    Themes.Light to lightColors()
)

enum class Themes {
    Dark, Light
}