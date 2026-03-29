package com.example.slidepuzzle.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.slidepuzzle.data.SettingsRepository
import com.example.slidepuzzle.ui.components.BlurredBeamsBackground
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsRepository: SettingsRepository,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val musicEnabled by settingsRepository.musicEnabled.collectAsState(initial = true)
    val sfxEnabled by settingsRepository.sfxEnabled.collectAsState(initial = true)
    val musicVolume by settingsRepository.musicVolume.collectAsState(initial = 0.5f)

    Box(modifier = Modifier.fillMaxSize()) {
        BlurredBeamsBackground()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Settings", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    tonalElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ListItem(
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                            headlineContent = { Text("Background Music", fontWeight = FontWeight.SemiBold) },
                            supportingContent = { Text("Enable soothing tunes") },
                            trailingContent = {
                                Switch(
                                    checked = musicEnabled,
                                    onCheckedChange = { scope.launch { settingsRepository.setMusicEnabled(it) } }
                                )
                            }
                        )
                        
                        if (musicEnabled) {
                            PaddingValues(horizontal = 16.dp).let {
                                Slider(
                                    value = musicVolume,
                                    onValueChange = { scope.launch { settingsRepository.setMusicVolume(it) } },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    tonalElevation = 2.dp
                ) {
                    ListItem(
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        headlineContent = { Text("Sound Effects", fontWeight = FontWeight.SemiBold) },
                        supportingContent = { Text("Feedback on tile slides") },
                        trailingContent = {
                            Switch(
                                checked = sfxEnabled,
                                onCheckedChange = { scope.launch { settingsRepository.setSfxEnabled(it) } }
                            )
                        }
                    )
                }
            }
        }
    }
}
