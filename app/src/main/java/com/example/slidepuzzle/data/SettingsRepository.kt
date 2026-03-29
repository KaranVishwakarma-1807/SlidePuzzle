package com.example.slidepuzzle.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {
    companion object {
        val MUSIC_ENABLED = booleanPreferencesKey("music_enabled")
        val SFX_ENABLED = booleanPreferencesKey("sfx_enabled")
        val MUSIC_VOLUME = floatPreferencesKey("music_volume")
        val CURRENT_PLAYER_ID = intPreferencesKey("current_player_id")
    }

    val musicEnabled: Flow<Boolean> = context.dataStore.data.map { it[MUSIC_ENABLED] ?: true }
    val sfxEnabled: Flow<Boolean> = context.dataStore.data.map { it[SFX_ENABLED] ?: true }
    val musicVolume: Flow<Float> = context.dataStore.data.map { it[MUSIC_VOLUME] ?: 0.5f }
    val currentPlayerId: Flow<Int?> = context.dataStore.data.map { it[CURRENT_PLAYER_ID] }

    suspend fun setMusicEnabled(enabled: Boolean) {
        context.dataStore.edit { it[MUSIC_ENABLED] = enabled }
    }

    suspend fun setSfxEnabled(enabled: Boolean) {
        context.dataStore.edit { it[SFX_ENABLED] = enabled }
    }

    suspend fun setMusicVolume(volume: Float) {
        context.dataStore.edit { it[MUSIC_VOLUME] = volume }
    }

    suspend fun setCurrentPlayerId(id: Int) {
        context.dataStore.edit { it[CURRENT_PLAYER_ID] = id }
    }
}
