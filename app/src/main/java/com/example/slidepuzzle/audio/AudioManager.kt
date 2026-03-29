package com.example.slidepuzzle.audio

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.slidepuzzle.data.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AudioManager(
    private val context: Context,
    private val settingsRepository: SettingsRepository
) {
    private val musicPlayer: ExoPlayer = ExoPlayer.Builder(context).build().apply {
        repeatMode = Player.REPEAT_MODE_ALL
    }
    private val sfxPlayer: ExoPlayer = ExoPlayer.Builder(context).build()

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var currentMusicResId: Int? = null

    init {
        scope.launch {
            combine(
                settingsRepository.musicEnabled,
                settingsRepository.musicVolume
            ) { enabled, volume ->
                enabled to volume
            }.collect { (enabled, volume) ->
                musicPlayer.volume = if (enabled) volume else 0f
                if (enabled) {
                    if (!musicPlayer.isPlaying && musicPlayer.mediaItemCount > 0) {
                        musicPlayer.play()
                    }
                } else {
                    if (musicPlayer.isPlaying) {
                        musicPlayer.pause()
                    }
                }
            }
        }
    }

    fun playMusic(resourceId: Int) {
        if (currentMusicResId == resourceId) return
        
        // Use try-catch or check existence to prevent crash if files are missing
        try {
            val uri = Uri.parse("android.resource://${context.packageName}/$resourceId")
            val mediaItem = MediaItem.fromUri(uri)
            
            musicPlayer.setMediaItem(mediaItem)
            musicPlayer.prepare()
            
            currentMusicResId = resourceId
            
            scope.launch {
                if (settingsRepository.musicEnabled.first()) {
                    musicPlayer.play()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playSfx(resourceId: Int) {
        scope.launch {
            if (settingsRepository.sfxEnabled.first()) {
                try {
                    val uri = Uri.parse("android.resource://${context.packageName}/$resourceId")
                    val mediaItem = MediaItem.fromUri(uri)
                    
                    sfxPlayer.setMediaItem(mediaItem)
                    sfxPlayer.prepare()
                    sfxPlayer.play()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun release() {
        musicPlayer.release()
        sfxPlayer.release()
        currentMusicResId = null
    }
}
