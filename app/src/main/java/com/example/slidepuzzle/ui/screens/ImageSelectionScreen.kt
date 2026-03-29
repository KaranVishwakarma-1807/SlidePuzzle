package com.example.slidepuzzle.ui.screens

import android.Manifest
import android.content.ActivityNotFoundException
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material.icons.rounded.Casino
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.slidepuzzle.data.PREDEFINED_IMAGES
import com.example.slidepuzzle.ui.components.BlurredBeamsBackground
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ImageSelectionScreen(
    onImageSelected: (Bitmap?, Int) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showSizeDialog by remember { mutableStateOf(false) }
    var pendingBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isDownloading by remember { mutableStateOf(false) }

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                    }
                }
                pendingBitmap = bitmap
                showSizeDialog = true
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            pendingBitmap = bitmap
            showSizeDialog = true
        }
    }

    fun downloadAndSelect(url: String) {
        scope.launch {
            isDownloading = true
            try {
                val loader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(url)
                    .allowHardware(false)
                    .build()
                
                val result = loader.execute(request)
                if (result is SuccessResult) {
                    pendingBitmap = (result.drawable as BitmapDrawable).bitmap
                    showSizeDialog = true
                } else {
                    Toast.makeText(context, "Failed to download image", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                isDownloading = false
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BlurredBeamsBackground()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Choose Challenge", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                // Action Buttons
                Surface(
                    modifier = Modifier.padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ActionChip(
                            icon = Icons.Rounded.Casino,
                            label = "Random",
                            onClick = {
                                val randomSize = Random.nextInt(3, 6)
                                val randomImage = PREDEFINED_IMAGES.random()
                                downloadAndSelect(randomImage.url)
                            }
                        )
                        ActionChip(
                            icon = Icons.Rounded.PhotoLibrary,
                            label = "Gallery",
                            onClick = { 
                                try {
                                    galleryLauncher.launch("image/*")
                                } catch (e: ActivityNotFoundException) {
                                    Toast.makeText(context, "No gallery app found", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                        ActionChip(
                            icon = Icons.Rounded.AddAPhoto,
                            label = "Camera",
                            onClick = {
                                if (cameraPermissionState.status.isGranted) {
                                    try {
                                        cameraLauncher.launch(null)
                                    } catch (e: ActivityNotFoundException) {
                                        Toast.makeText(context, "No camera app found", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    cameraPermissionState.launchPermissionRequest()
                                }
                            }
                        )
                    }
                }

                Text(
                    "Image Library",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(PREDEFINED_IMAGES) { image ->
                        Card(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable { downloadAndSelect(image.url) },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            AsyncImage(
                                model = image.url,
                                contentDescription = image.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
            
            if (isDownloading) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }

    if (showSizeDialog) {
        AlertDialog(
            onDismissRequest = { 
                showSizeDialog = false 
                pendingBitmap = null
            },
            title = { Text("Select Difficulty", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    listOf(3, 4, 5, 6).forEach { size ->
                        ListItem(
                            headlineContent = { Text("${size}x${size} Grid", fontWeight = FontWeight.Medium) },
                            supportingContent = { Text("${size * size - 1} tiles to move") },
                            modifier = Modifier.clickable {
                                showSizeDialog = false
                                onImageSelected(pendingBitmap, size)
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { 
                    showSizeDialog = false 
                    pendingBitmap = null
                }) {
                    Text("CANCEL")
                }
            },
            shape = RoundedCornerShape(28.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    ElevatedFilterChip(
        selected = false,
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp)) },
        shape = MaterialTheme.shapes.large
    )
}
