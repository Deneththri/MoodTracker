package com.example.moodtracker.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodtracker.data.MoodEntry
import com.example.moodtracker.viewmodel.MoodViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MoodViewModel,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToReport: () -> Unit
) {
    val moodEntries by viewModel.moodEntries.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Mood Tracker") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = onNavigateToReport) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Weekly Report"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Mood Selector Section
            Text(
                text = "How are you feeling today?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            MoodSelector(
                moods = viewModel.availableMoods,
                onMoodSelected = { mood ->
                    viewModel.addMoodEntry(mood)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mood History Section
            Text(
                text = "Mood History",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (moodEntries.isEmpty()) {
                EmptyStateView()
            } else {
                MoodHistoryList(
                    moodEntries = moodEntries,
                    onMoodClick = onNavigateToDetail,
                    onDeleteClick = { entry ->
                        viewModel.deleteMoodEntry(entry)
                    }
                )
            }
        }
    }
}

@Composable
fun MoodSelector(
    moods: List<String>,
    onMoodSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        moods.forEach { mood ->
            MoodCard(
                mood = mood,
                onClick = { onMoodSelected(mood) }
            )
        }
    }
}

@Composable
fun MoodCard(
    mood: String,
    onClick: () -> Unit
) {
    val emoji = mood.split(" ").lastOrNull() ?: ""
    val moodName = mood.split(" ").firstOrNull() ?: mood

    val cardColor = when (moodName) {
        "Happy" -> MaterialTheme.colorScheme.tertiaryContainer
        "Calm" -> MaterialTheme.colorScheme.secondaryContainer
        "Neutral" -> MaterialTheme.colorScheme.surfaceVariant
        "Sad" -> MaterialTheme.colorScheme.primaryContainer
        "Anxious" -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surface
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .size(70.dp)
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = emoji,
                fontSize = 32.sp
            )
        }
    }
}

@Composable
fun EmptyStateView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🌟",
                fontSize = 64.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No moods logged yet.",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "How are you feeling today?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MoodHistoryList(
    moodEntries: List<MoodEntry>,
    onMoodClick: (Long) -> Unit,
    onDeleteClick: (MoodEntry) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(moodEntries, key = { it.id }) { entry ->
            MoodHistoryItem(
                entry = entry,
                onClick = { onMoodClick(entry.id) },
                onDeleteClick = { onDeleteClick(entry) }
            )
        }
    }
}

@Composable
fun MoodHistoryItem(
    entry: MoodEntry,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.mood,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = entry.timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (entry.note.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = entry.note,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2
                    )
                }
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}