package com.example.moodtracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_entries ORDER BY id DESC")
    fun getAllMoods(): Flow<List<MoodEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(moodEntry: MoodEntry)

    @Update
    suspend fun updateMood(moodEntry: MoodEntry)

    @Delete
    suspend fun deleteMood(moodEntry: MoodEntry)

    @Query("SELECT * FROM mood_entries WHERE id = :id")
    suspend fun getMoodById(id: Long): MoodEntry?

    @Query("SELECT * FROM mood_entries WHERE timestamp >= :startDate ORDER BY id DESC")
    fun getMoodsFromDate(startDate: String): Flow<List<MoodEntry>>
}