package com.example.slidepuzzle.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "high_scores")
data class HighScore(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val playerId: Int,
    val gridSize: Int, // e.g., 3 for 3x3
    val moves: Int,
    val timeInSeconds: Long,
    val timestamp: Long = System.currentTimeMillis()
)
