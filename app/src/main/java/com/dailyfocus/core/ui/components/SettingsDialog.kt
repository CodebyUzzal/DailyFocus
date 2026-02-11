package com.dailyfocus.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.dailyfocus.core.preferences.AppPreferences
import com.dailyfocus.core.ui.theme.Spacing
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    appPreferences: AppPreferences
) {
    val scope = rememberCoroutineScope()
    val fontScale by appPreferences.fontScale.collectAsState(initial = 1.0f)
    val themeMode by appPreferences.themeMode.collectAsState(initial = AppPreferences.THEME_SYSTEM)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Appearance") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.l)) {
                
                // ── Font Scale ──────────────────────────────────────────
                Column {
                    Text(
                        text = "Font Scale: ${(fontScale * 100).roundToInt()}%",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Slider(
                        value = fontScale,
                        onValueChange = { 
                            scope.launch { appPreferences.setFontScale(it) } 
                        },
                        valueRange = 0.85f..1.30f,
                        steps = 8 // ~5% increments
                    )
                    Text(
                        text = "Preview Text Size",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // ── Theme Mode ──────────────────────────────────────────
                Column {
                    Text(
                        text = "Theme",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(Spacing.s))
                    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.s)) {
                        FilterChip(
                            selected = themeMode == AppPreferences.THEME_SYSTEM,
                            onClick = { scope.launch { appPreferences.setThemeMode(AppPreferences.THEME_SYSTEM) } },
                            label = { Text("System") }
                        )
                        FilterChip(
                            selected = themeMode == AppPreferences.THEME_LIGHT,
                            onClick = { scope.launch { appPreferences.setThemeMode(AppPreferences.THEME_LIGHT) } },
                            label = { Text("Light") }
                        )
                        FilterChip(
                            selected = themeMode == AppPreferences.THEME_DARK,
                            onClick = { scope.launch { appPreferences.setThemeMode(AppPreferences.THEME_DARK) } },
                            label = { Text("Dark") }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
