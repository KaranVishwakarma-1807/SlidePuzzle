package com.example.slidepuzzle.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_profiles")
data class PlayerProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val avatarUri: String? = null,
    val totalGamesPlayed: Int = 0,
    val totalWins: Int = 0
)
