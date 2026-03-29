package com.example.slidepuzzle.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM player_profiles")
    fun getAllPlayers(): Flow<List<PlayerProfile>>

    @Query("SELECT * FROM player_profiles WHERE id = :id")
    suspend fun getPlayerById(id: Int): PlayerProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: PlayerProfile): Long

    @Update
    suspend fun updatePlayer(player: PlayerProfile)

    @Delete
    suspend fun deletePlayer(player: PlayerProfile)

    @Query("SELECT * FROM high_scores WHERE playerId = :playerId ORDER BY moves ASC, timeInSeconds ASC")
    fun getHighScoresForPlayer(playerId: Int): Flow<List<HighScore>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHighScore(highScore: HighScore)
}
