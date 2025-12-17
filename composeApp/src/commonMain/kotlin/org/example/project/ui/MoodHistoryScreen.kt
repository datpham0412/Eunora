package org.example.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.MoodEntry
import org.example.project.ui.mood_result.CalmSurface
import org.example.project.ui.mood_result.formatMoodName
import org.example.project.ui.mood_result.getMoodEmoji
import viewmodel.MoodHistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodHistoryScreen(
    viewModel: MoodHistoryViewModel,
    onEntryClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.state.collectAsState()

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFCDB8F0),
            Color(0xFFB8C8F5),
            Color(0xFFE8C5E6),
            Color(0xFFC8CDF3)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mood History",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("←", fontSize = 24.sp, color = Color(0xFF1F2937))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color(0xFF1F2937)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF9470F4)
                    )
                }

                uiState.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "⚠️",
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = uiState.error ?: "Unknown error",
                            color = Color(0xFF742A2A),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                }

                uiState.groupedEntries.isEmpty() -> {
                    EmptyHistoryView(modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        uiState.groupedEntries.forEach { (dateLabel, entries) ->
                            item(key = "header_$dateLabel") {
                                DateHeader(dateLabel)
                            }

                            items(entries, key = { it.id }) { entry ->
                                MoodHistoryCard(
                                    entry = entry,
                                    onClick = { onEntryClick(entry.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onCleared()
        }
    }
}

@Composable
private fun DateHeader(dateLabel: String) {
    Text(
        text = "📅 $dateLabel",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1F2937),
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
    )
}

@Composable
private fun MoodHistoryCard(
    entry: MoodEntry,
    onClick: () -> Unit
) {
    CalmSurface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header row: emoji + mood + time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = getMoodEmoji(entry.normalizedMood),
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = formatMoodName(entry.normalizedMood),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                }

                Text(
                    text = formatTime(entry.timestamp),
                    fontSize = 14.sp,
                    color = Color(0xFF64748B)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Journal preview (2 lines max)
            Text(
                text = entry.ai.journal,
                fontSize = 15.sp,
                color = Color(0xFF475569),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 22.sp
            )

            // Highlight badge (if available)
            entry.highlight?.let { highlight ->
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = Color(0xFFFFF8E1),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "✨",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = highlight,
                        fontSize = 14.sp,
                        color = Color(0xFF92400E),
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mini spectrum visualization
            MiniEmotionalSpectrum(
                positivity = entry.ai.emotion.positivity,
                energy = entry.ai.emotion.energy,
                stress = entry.ai.emotion.stress
            )
        }
    }
}

@Composable
private fun MiniEmotionalSpectrum(
    positivity: Float,
    energy: Float,
    stress: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MiniSpectrumBar(
            value = positivity,
            color = Color(0xFF10B981),
            modifier = Modifier.weight(1f)
        )
        MiniSpectrumBar(
            value = energy,
            color = Color(0xFFF59E0B),
            modifier = Modifier.weight(1f)
        )
        MiniSpectrumBar(
            value = stress,
            color = Color(0xFFF97316),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MiniSpectrumBar(
    value: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(color.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(value.coerceIn(0f, 1f))
                .background(color)
        )
    }
}

@Composable
private fun EmptyHistoryView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "📖",
            fontSize = 64.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No entries yet",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1F2937)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Start journaling to build your emotional timeline",
            fontSize = 15.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center
        )
    }
}

private fun formatTime(timestamp: Long): String {
    // Simple time formatting without kotlinx.datetime
    val totalMinutes = ((timestamp / 1000 / 60) % (24 * 60)).toInt()
    val hour = totalMinutes / 60
    val minute = totalMinutes % 60
    val amPm = if (hour < 12) "AM" else "PM"
    val hour12 = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
    return "$hour12:${minute.toString().padStart(2, '0')} $amPm"
}
