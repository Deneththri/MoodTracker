package com.example.moodtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodtracker.data.MoodDatabase
import com.example.moodtracker.data.MoodEntry
import com.example.moodtracker.data.MoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MoodViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MoodRepository
    private val _moodEntries = MutableStateFlow<List<MoodEntry>>(emptyList())
    val moodEntries: StateFlow<List<MoodEntry>> = _moodEntries.asStateFlow()

    val availableMoods = listOf(
        "Happy 😊",
        "Calm 🙂",
        "Neutral 😐",
        "Sad 😟",
        "Anxious 😬"
    )

    init {
        val moodDao = MoodDatabase.getDatabase(application).moodDao()
        repository = MoodRepository(moodDao)
        loadMoods()
    }

    private fun loadMoods() {
        viewModelScope.launch {
            repository.allMoods.collect { moods ->
                _moodEntries.value = moods
            }
        }
    }

    fun addMoodEntry(mood: String) {
        viewModelScope.launch {
            val newEntry = MoodEntry(mood = mood)
            repository.insertMood(newEntry)
        }
    }

    fun deleteMoodEntry(moodEntry: MoodEntry) {
        viewModelScope.launch {
            repository.deleteMood(moodEntry)
        }
    }

    fun updateMoodEntry(moodEntry: MoodEntry) {
        viewModelScope.launch {
            repository.updateMood(moodEntry)
        }
    }

    suspend fun getMoodById(id: Long): MoodEntry? {
        return repository.getMoodById(id)
    }

    fun getWeeklyMoodData(): Map<String, Int> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val sevenDaysAgo = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(calendar.time)

        val moodCounts = mutableMapOf<String, Int>()
        availableMoods.forEach { mood ->
            moodCounts[mood] = 0
        }

        _moodEntries.value.forEach { entry ->
            val moodBase = entry.mood
            moodCounts[moodBase] = (moodCounts[moodBase] ?: 0) + 1
        }

        return moodCounts
    }
}