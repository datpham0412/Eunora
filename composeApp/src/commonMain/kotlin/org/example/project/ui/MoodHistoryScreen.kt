package org.example.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.MoodEntry
import model.NormalizedMood
import org.example.project.ui.mood_result.CalmSurface
import org.example.project.ui.mood_result.formatMoodName
import org.example.project.ui.mood_result.getMoodEmoji
import viewmodel.MoodFilter
import viewmodel.MoodHistoryViewModel

@Composable
fun MoodHistoryScreen(
    viewModel: MoodHistoryViewModel,
    onEntryClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    // Theme Colors (Matching Mockup)
    val BackgroundColor = Color(0xFFE2F4F2) // Light Mint
    val PrimaryText = Color(0xFF134E4A) // Dark Teal
    val SecondaryText = Color(0xFF134E4A).copy(alpha = 0.7f)
    val AccentColor = Color(0xFF2D8A7F)
    val CardColor = Color.White
    
    Scaffold(
        containerColor = BackgroundColor,
        contentColor = PrimaryText
    ) { padding ->
        

        // Handle Back (System)
        BackHandler(onBack = onBackClick)

        // Handle Back (System/Swipe)
         // Native-style Back Button (Top Left)
         // We place it in the Box below to layer correctly or strictly here?
         // Let's rely on Box approach for consistency with Input Screen.

        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header Space for Back Button
                Spacer(modifier = Modifier.height(16.dp))
                
                // Adaptive Back Button
                AdaptiveBackButton(onClick = onBackClick, modifier = Modifier.padding(start = 8.dp))

                Spacer(modifier = Modifier.height(16.dp))

                // Scrollable Content
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    // Header Item
                    item {
                        Column {
                            Text(
                                "Your Mood Journey",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryText
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Track your emotional patterns over time",
                                fontSize = 16.sp,
                                color = SecondaryText,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }

                    // Stats Row
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatsCard(
                                value = state.totalEntries.toString(),
                                label = "Total Entries",
                                modifier = Modifier.weight(1f)
                            )
                            StatsCard(
                                value = state.averageMood, // Using N/A or computed
                                label = "Avg Mood",
                                modifier = Modifier.weight(1f)
                            )
                            StatsCard(
                                value = state.mostCommonMood,
                                label = "Most Common", // Emoji
                                modifier = Modifier.weight(1f),
                                isEmoji = true
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    // Timeline Header & Filters
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Timeline",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryText
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            // Filter Chips
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(MoodFilter.values()) { filter ->
                                    FilterChipItem(
                                        filter = filter,
                                        isSelected = state.selectedFilter == filter,
                                        onClick = { viewModel.setFilter(filter) }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Grouped List
                    state.groupedFilteredEntries.forEach { (dateHeader, entries) ->
                        // Date Header (optional if we want headers like "Today")
                        // Mockup shows just timeline list. But date grouping helps context.
                        // I'll show a small header if it's not "Timeline"
                        // Or just flatten the list? The ViewModel provides grouped map. I'll iterate.
                        
                        item {
                            Text(
                                text = dateHeader,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SecondaryText,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        items(entries) { entry ->
                             MoodHistoryCard(entry = entry, onClick = { onEntryClick(entry.id) })
                             Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                    
                    if (state.groupedFilteredEntries.isEmpty() && !state.isLoading) {
                         item {
                             Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                                 Text("No entries found", color = SecondaryText)
                             }
                         }
                    }
                }
            }
            
            // Edge Swipe (iOS)
            EdgeSwipeBackHandler(onBack = onBackClick)
        }
    }
}

@Composable
fun StatsCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    isEmoji: Boolean = false
) {
    Surface(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = if (isEmoji) 32.sp else 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF134E4A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFF134E4A).copy(alpha = 0.7f),
                lineHeight = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun FilterChipItem(
    filter: MoodFilter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) Color(0xFFB2DFDB) else Color.Transparent, // Light Teal if selected
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = filter.displayName,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = Color(0xFF134E4A).copy(alpha = if (isSelected) 1f else 0.6f)
        )
    }
}

@Composable
fun MoodHistoryCard(entry: MoodEntry, onClick: () -> Unit) {
    // Custom Chevron Right Vector
    val ChevronRightIcon = remember {
        ImageVector.Builder(
            name = "ChevronRight",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF134E4A)),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f
            ) {
                moveTo(10f, 6f)
                lineTo(8.59f, 7.41f)
                lineTo(13.17f, 12f)
                lineTo(8.59f, 16.59f)
                lineTo(10f, 18f)
                lineTo(16f, 12f)
                close()
            }
        }.build()
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp) // increased height for spectrum
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji Circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(getColorForMood(entry.normalizedMood).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(getMoodEmoji(entry.normalizedMood), fontSize = 24.sp)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Text Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = formatMoodName(entry.normalizedMood),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF134E4A)
                )
                Text(
                    text = if (entry.ai.journal.isNotBlank()) entry.ai.journal else "No details...",
                    fontSize = 14.sp,
                    color = Color(0xFF134E4A).copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Mini spectrum visualization
                MiniEmotionalSpectrum(
                    positivity = entry.ai.emotion.positivity,
                    energy = entry.ai.emotion.energy,
                    stress = entry.ai.emotion.stress
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Chevron
            Icon(
                imageVector = ChevronRightIcon,
                contentDescription = null,
                tint = Color(0xFF134E4A).copy(alpha = 0.4f)
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

private fun getColorForMood(mood: NormalizedMood): Color {
    return when (mood) {
        NormalizedMood.HAPPY_ENERGETIC, NormalizedMood.EXCITED -> Color(0xFFFDE68A) // Yellow
        NormalizedMood.CALM_POSITIVE, NormalizedMood.NEUTRAL -> Color(0xFFA7F3D0) // Green
        NormalizedMood.SAD, NormalizedMood.DEPRESSED -> Color(0xFFBFDBFE) // Blue
        NormalizedMood.STRESSED, NormalizedMood.ANXIOUS, NormalizedMood.ANGRY, NormalizedMood.OVERWHELMED -> Color(0xFFFECACA) // Red
    }
}
