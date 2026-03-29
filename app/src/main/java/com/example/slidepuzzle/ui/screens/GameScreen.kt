package com.example.slidepuzzle.ui.screens

import android.graphics.Bitmap
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.slidepuzzle.R
import com.example.slidepuzzle.audio.AudioManager
import com.example.slidepuzzle.logic.ImageSplitter
import com.example.slidepuzzle.logic.PuzzleTile
import com.example.slidepuzzle.ui.components.BlurredBeamsBackground
import com.example.slidepuzzle.ui.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    gridSize: Int,
    imageBitmap: Bitmap? = null,
    audioManager: AudioManager? = null,
    onBack: () -> Unit,
    viewModel: GameViewModel = viewModel()
) {
    val tiles by viewModel.tiles.collectAsState()
    val moves by viewModel.moves.collectAsState()
    val timeSeconds by viewModel.timeSeconds.collectAsState()
    val isSolved by viewModel.isSolved.collectAsState()
    val currentGridSize by viewModel.gridSize.collectAsState()
    
    var showPreview by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.startGame(gridSize, imageBitmap)
    }

    LaunchedEffect(isSolved) {
        if (isSolved) {
            audioManager?.playSfx(R.raw.win_sound)
        }
    }

    val splitBitmaps = remember(tiles, imageBitmap, currentGridSize) {
        if (imageBitmap != null && currentGridSize > 0) {
            ImageSplitter.splitImage(imageBitmap, currentGridSize)
        } else {
            emptyList()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // "Alive" Background
        BlurredBeamsBackground()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        Column {
                            Text("SlidePuzzle", style = MaterialTheme.typography.titleMedium)
                            Text("${currentGridSize}x${currentGridSize} Grid", style = MaterialTheme.typography.labelSmall)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { showPreview = !showPreview }) {
                            Icon(Icons.Rounded.Info, contentDescription = "Show Preview")
                        }
                        IconButton(onClick = { viewModel.resetGame() }) {
                            Icon(Icons.Rounded.Refresh, contentDescription = "Reset")
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
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Cozy Stats Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(label = "MOVES", value = moves.toString())
                        VerticalDivider(modifier = Modifier.height(40.dp).padding(horizontal = 16.dp))
                        StatItem(label = "TIME", value = formatTime(timeSeconds))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Mini Preview Overlay Toggle
                AnimatedVisibility(
                    visible = showPreview,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Card(
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .size(120.dp)
                            .shadow(8.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        if (imageBitmap != null) {
                            Image(
                                bitmap = imageBitmap.asImageBitmap(),
                                contentDescription = "Preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.secondaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Preview", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }

                // Puzzle Grid with Cozy Styling
                BoxWithConstraints(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(28.dp))
                        .clip(RoundedCornerShape(28.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                        .padding(8.dp)
                ) {
                    val boardSize = maxWidth
                    val tileSize = boardSize / currentGridSize

                    tiles.forEachIndexed { index, tile ->
                        PuzzleTileItem(
                            tile = tile,
                            index = index,
                            gridSize = currentGridSize,
                            tileSize = tileSize,
                            bitmap = if (!tile.isEmpty && splitBitmaps.isNotEmpty()) splitBitmaps[tile.originalIndex] else null,
                            onClick = { 
                                audioManager?.playSfx(R.raw.tile_tap)
                                viewModel.onTileClicked(index) 
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Win Feedback
                AnimatedVisibility(
                    visible = isSolved,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut()
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        tonalElevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "✨ Puzzle Solved! ✨",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "You finished in $moves moves and ${formatTime(timeSeconds)}!",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.resetGame() },
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("PLAY AGAIN", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PuzzleTileItem(
    tile: PuzzleTile,
    index: Int,
    gridSize: Int,
    tileSize: Dp,
    bitmap: Bitmap?,
    onClick: () -> Unit
) {
    val row = index / gridSize
    val col = index % gridSize

    val offsetX by animateDpAsState(
        targetValue = col.dp * tileSize.value,
        animationSpec = tween(durationMillis = 300),
        label = "offset_x"
    )
    val offsetY by animateDpAsState(
        targetValue = row.dp * tileSize.value,
        animationSpec = tween(durationMillis = 300),
        label = "offset_y"
    )

    Box(
        modifier = Modifier
            .size(tileSize)
            .offset(x = offsetX, y = offsetY)
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (tile.isEmpty) Color.Transparent else MaterialTheme.colorScheme.primaryContainer)
            .clickable(enabled = !tile.isEmpty) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (!tile.isEmpty) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = (tile.originalIndex + 1).toString(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label, 
            style = MaterialTheme.typography.labelSmall, 
            color = MaterialTheme.colorScheme.secondary,
            letterSpacing = 1.sp
        )
        Text(
            text = value, 
            style = MaterialTheme.typography.headlineSmall, 
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

private fun formatTime(seconds: Long): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}
