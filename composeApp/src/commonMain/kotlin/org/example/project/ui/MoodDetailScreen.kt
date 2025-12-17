package org.example.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.MoodEntry
import org.example.project.ui.mood_result.*
import viewmodel.MoodDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodDetailScreen(
    viewModel: MoodDetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("←", fontSize = 24.sp, color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF9470F4)
                    )
                }
            }

            uiState.entry != null -> {
                MoodDetailContent(
                    entry = uiState.entry!!,
                    modifier = Modifier.padding(padding)
                )
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "❌",
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = uiState.error ?: "Entry not found",
                            color = Color(0xFF742A2A),
                            textAlign = TextAlign.Center
                        )
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
private fun MoodDetailContent(
    entry: MoodEntry,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Abstract background
        MoodAbstractBackground(entry.normalizedMood)

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp)
                .padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = getMoodEmoji(entry.normalizedMood),
                    fontSize = 64.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = formatMoodName(entry.normalizedMood),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.95f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatFullDateTime(entry.timestamp),
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.75f)
                )
            }

            // Highlight section (if available)
            entry.highlight?.let { highlight ->
                CalmSurface(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Text(text = "✨", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Small Moment",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F2937)
                            )
                        }

                        Text(
                            text = "\"$highlight\"",
                            fontSize = 18.sp,
                            color = Color(0xFF92400E),
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp
                        )
                    }
                }
            }

            // Reflection section
            CalmSurface(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(32.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Text(text = "✨", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Your Reflection",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                    }

                    Text(
                        text = entry.ai.journal,
                        fontSize = 16.sp,
                        color = Color(0xFF475569),
                        lineHeight = 26.sp
                    )
                }
            }

            // Emotional spectrums
            CalmSurface(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = "How you're feeling",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )

                    // Spectrum 1: Emotional tone
                    EmotionalSpectrum(
                        label = "Emotional tone",
                        leftLabel = "Difficult",
                        rightLabel = "Positive",
                        position = entry.ai.emotion.positivity,
                        gradient = Brush.horizontalGradient(
                            listOf(
                                Color(0xFF9CA3AF).copy(alpha = 0.3f),
                                Color(0xFF10B981).copy(alpha = 0.3f)
                            )
                        ),
                        markerColor = Color(0xFF10B981),
                        isVisible = true
                    )

                    // Spectrum 2: Energy level
                    EmotionalSpectrum(
                        label = "Energy level",
                        leftLabel = "Resting",
                        rightLabel = "Energized",
                        position = entry.ai.emotion.energy,
                        gradient = Brush.horizontalGradient(
                            listOf(
                                Color(0xFFA78BFA).copy(alpha = 0.3f),
                                Color(0xFFF59E0B).copy(alpha = 0.3f)
                            )
                        ),
                        markerColor = Color(0xFFF59E0B),
                        isVisible = true
                    )

                    // Spectrum 3: Inner state
                    EmotionalSpectrum(
                        label = "Inner state",
                        leftLabel = "At ease",
                        rightLabel = "Tense",
                        position = entry.ai.emotion.stress,
                        gradient = Brush.horizontalGradient(
                            listOf(
                                Color(0xFF10B981).copy(alpha = 0.3f),
                                Color(0xFFF97316).copy(alpha = 0.3f)
                            )
                        ),
                        markerColor = if (entry.ai.emotion.stress > 0.6f)
                            Color(0xFFF97316) else Color(0xFF10B981),
                        isVisible = true
                    )
                }
            }

            // Guidance section
            CalmSurface(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(32.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Text(text = "💡", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Gentle Guidance",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                    }

                    Text(
                        text = entry.ai.advice,
                        fontSize = 16.sp,
                        color = Color(0xFF475569),
                        lineHeight = 26.sp
                    )
                }
            }
        }
    }
}

private fun formatFullDateTime(timestamp: Long): String {
    // Simple date/time formatting without kotlinx.datetime
    val daysSinceEpoch = (timestamp / (24 * 60 * 60 * 1000L)).toInt()
    val year = 1970 + (daysSinceEpoch / 365)
    val dayOfYear = daysSinceEpoch % 365

    val monthDays = listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun",
                           "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    var remainingDays = dayOfYear
    var monthIndex = 0
    for (i in monthDays.indices) {
        if (remainingDays <= monthDays[i]) {
            monthIndex = i
            break
        }
        remainingDays -= monthDays[i]
    }

    val day = remainingDays + 1
    val month = monthNames.getOrNull(monthIndex) ?: "Jan"

    // Time calculation
    val totalMinutes = ((timestamp / 1000 / 60) % (24 * 60)).toInt()
    val hour = totalMinutes / 60
    val minute = totalMinutes % 60
    val amPm = if (hour < 12) "AM" else "PM"
    val hour12 = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour

    return "$month $day, $year • $hour12:${minute.toString().padStart(2, '0')} $amPm"
}
