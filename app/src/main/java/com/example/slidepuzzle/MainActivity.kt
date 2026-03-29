package com.example.slidepuzzle

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.slidepuzzle.audio.AudioManager
import com.example.slidepuzzle.data.AppDatabase
import com.example.slidepuzzle.data.SettingsRepository
import com.example.slidepuzzle.ui.screens.*
import com.example.slidepuzzle.ui.theme.SlidePuzzleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val settingsRepository = remember { SettingsRepository(context) }
            val audioManager = remember { AudioManager(context, settingsRepository) }
            val database = remember { AppDatabase.getDatabase(context) }

            var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

            DisposableEffect(Unit) {
                onDispose {
                    audioManager.release()
                }
            }

            SlidePuzzleTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            // Home BGM
                            LaunchedEffect(Unit) {
                                audioManager.playMusic(R.raw.homescreen_music)
                            }
                            
                            HomeScreen(
                                onNavigateToGame = { 
                                    navController.navigate("image_selection") 
                                },
                                onNavigateToSettings = { navController.navigate("settings") },
                                onNavigateToProfile = { navController.navigate("profile") }
                            )
                        }
                        composable("image_selection") {
                            // Home BGM continues here usually, but we could play something else
                            ImageSelectionScreen(
                                onImageSelected = { bitmap, size ->
                                    selectedBitmap = bitmap
                                    navController.navigate("game/$size")
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable(
                            route = "game/{size}",
                            arguments = listOf(navArgument("size") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val size = backStackEntry.arguments?.getInt("size") ?: 3
                            
                            // Game BGM
                            LaunchedEffect(Unit) {
                                audioManager.playMusic(R.raw.gamescreen_music)
                            }

                            GameScreen(
                                gridSize = size,
                                imageBitmap = selectedBitmap,
                                audioManager = audioManager,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                settingsRepository = settingsRepository,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("profile") {
                            ProfileScreen(
                                playerDao = database.playerDao(),
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
