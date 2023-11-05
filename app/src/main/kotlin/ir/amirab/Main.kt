package ir.amirab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ir.amirab.Themes.*
import ir.amirab.customwindow.CustomWindow
import ir.amirab.customwindow.WindowCenter
import ir.amirab.customwindow.WindowIcon
import ir.amirab.customwindow.WindowTitle


fun main() = application {

    val state = rememberWindowState()
    var selectedTheme by remember { mutableStateOf(Dark) }
    MaterialTheme(themes[selectedTheme]!!) {
        CustomWindow(state, {
            exitApplication()
        }) {
            WindowTitle("Custom Window")
            WindowCenter {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                ) {
                    Icon(
                        when (selectedTheme) {
                            Dark -> Icons.Default.LightMode
                            Light -> Icons.Default.DarkMode
                        },
                        null,
                        Modifier
                            .windowFrameItem("theme", HitSpots.OTHER_HIT_SPOT)
                            .clickable {
                                selectedTheme = when (selectedTheme) {
                                    Dark -> Light
                                    Light -> Dark
                                }
                            }
                            .padding(4.dp)
                            .size(16.dp)
                            .clip(CircleShape)
                    )
                }
            }
            Column(Modifier.fillMaxSize().wrapContentSize()) {
                Text("Hello from compose multiplatform", style = MaterialTheme.typography.h5)
                Text("This window supports aero snap. drag to edge and see the results", style = MaterialTheme.typography.body1)
            }
        }
    }
}