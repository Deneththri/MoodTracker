package com.example.moodtracker.data

import kotlinx.coroutines.flow.Flow

class MoodRepository(private val moodDao: MoodDao) {
    val allMoods: Flow<List<MoodEntry>> = moodDao.getAllMoods()

    suspend fun insertMood(moodEntry: MoodEntry) {
        moodDao.insertMood(moodEntry)
    }

    suspend fun updateMood(moodEntry: MoodEntry) {
        moodDao.updateMood(moodEntry)
    }

    suspend fun deleteMood(moodEntry: MoodEntry) {
        moodDao.deleteMood(moodEntry)
    }

    suspend fun getMoodById(id: Long): MoodEntry? {
        return moodDao.getMoodById(id)
    }

    fun getMoodsFromDate(startDate: String): Flow<List<MoodEntry>> {
        return moodDao.getMoodsFromDate(startDate)
    }
}