package org.example.project.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.MoodEntry
import model.NormalizedMood

@Composable
fun MoodResultScreen(
    moodEntry: MoodEntry,
    onNewMood: () -> Unit
) {
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFCDB8F0),
            Color(0xFFB8C8F5),
            Color(0xFFE8C5E6),
            Color(0xFFC8CDF3)
        )
    )

    val buttonGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF9470F4),
            Color(0xFF5E82F3),
            Color(0xFFD770C5)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "Your Mood Reflection",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 34.sp,
                    lineHeight = 42.sp
                ),
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937),
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Understanding your emotional landscape",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 17.sp,
                    lineHeight = 24.sp
                ),
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4A5568).copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Mood Badge
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                color = Color.White,
                tonalElevation = 0.dp,
                shadowElevation = 3.dp
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.verticalGradient(
                            listOf(
                                getMoodColor(moodEntry.normalizedMood).copy(alpha = 0.15f),
                                getMoodColor(moodEntry.normalizedMood).copy(alpha = 0.25f)
                            )
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(28.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(
                                    getMoodColor(moodEntry.normalizedMood).copy(alpha = 0.2f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = getMoodEmoji(moodEntry.normalizedMood),
                                fontSize = 40.sp
                            )
                        }
                        Column {
                            Text(
                                text = formatMoodName(moodEntry.normalizedMood),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 24.sp
                                ),
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1F2937)
                            )
                            Text(
                                text = "Detected mood",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 15.sp
                                ),
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF4A5568).copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Emotion Scores Section
            Text(
                text = "Emotion Scores",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp
                ),
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F2937),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(16.dp))

            EmotionScoreCard(
                label = "Positivity",
                score = moodEntry.ai.emotion.positivity,
                color = Color(0xFF10B981),
                icon = "✨"
            )

            Spacer(modifier = Modifier.height(12.dp))

            EmotionScoreCard(
                label = "Energy",
                score = moodEntry.ai.emotion.energy,
                color = Color(0xFFF59E0B),
                icon = "⚡"
            )

            Spacer(modifier = Modifier.height(12.dp))

            EmotionScoreCard(
                label = "Stress",
                score = moodEntry.ai.emotion.stress,
                color = Color(0xFFEF4444),
                icon = "💫"
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Journal Entry
            SectionCard(
                title = "AI Journal",
                content = moodEntry.ai.journal,
                icon = "📝"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Advice
            SectionCard(
                title = "Personalized Advice",
                content = moodEntry.ai.advice,
                icon = "💡"
            )

            Spacer(modifier = Modifier.height(40.dp))

            // New Entry Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(buttonGradient)
            ) {
                Button(
                    onClick = onNewMood,
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
                ) {
                    Text(
                        "New Mood Entry",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 17.sp,
                            letterSpacing = 0.5.sp
                        ),
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun EmotionScoreCard(label: String, score: Float, color: Color, icon: String) {
    var animatedProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(score) {
        animate(
            initialValue = 0f,
            targetValue = score,
            animationSpec = tween(durationMillis = 1200, easing = EaseOutCubic)
        ) { value, _ ->
            animatedProgress = value
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        tonalElevation = 0.dp,
        shadowElevation = 2.dp
    ) {
        Box(
            modifier = Modifier.background(
                Brush.verticalGradient(
                    listOf(
                        Color.White,
                        color.copy(alpha = 0.05f)
                    )
                )
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = icon,
                            fontSize = 20.sp
                        )
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 17.sp
                            ),
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1F2937)
                        )
                    }
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 20.sp
                        ),
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(color.copy(alpha = 0.15f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(5.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        color,
                                        color.copy(alpha = 0.8f)
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun SectionCard(title: String, content: String, icon: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        tonalElevation = 0.dp,
        shadowElevation = 2.dp
    ) {
        Box(
            modifier = Modifier.background(
                Brush.verticalGradient(
                    listOf(
                        Color.White,
                        Color(0xFFFAF5FF)
                    )
                )
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "$icon $title",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 19.sp
                    ),
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        lineHeight = 26.sp
                    ),
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF4A5568).copy(alpha = 0.85f),
                    letterSpacing = 0.2.sp
                )
            }
        }
    }
}

fun getMoodColor(mood: NormalizedMood): Color {
    return when (mood) {
        NormalizedMood.CALM_POSITIVE -> Color(0xFF10B981)      // Green
        NormalizedMood.HAPPY_ENERGETIC -> Color(0xFFF59E0B)    // Orange
        NormalizedMood.EXCITED -> Color(0xFFEC4899)            // Pink
        NormalizedMood.NEUTRAL -> Color(0xFF6B7280)            // Gray
        NormalizedMood.STRESSED -> Color(0xFFF97316)           // Dark Orange
        NormalizedMood.ANXIOUS -> Color(0xFF8B5CF6)            // Purple
        NormalizedMood.SAD -> Color(0xFF3B82F6)                // Blue
        NormalizedMood.DEPRESSED -> Color(0xFF6366F1)          // Indigo
        NormalizedMood.ANGRY -> Color(0xFFEF4444)              // Red
        NormalizedMood.OVERWHELMED -> Color(0xFFDC2626)        // Dark Red
    }
}

fun getMoodEmoji(mood: NormalizedMood): String {
    return when (mood) {
        NormalizedMood.CALM_POSITIVE -> "😌"
        NormalizedMood.HAPPY_ENERGETIC -> "😊"
        NormalizedMood.EXCITED -> "🤩"
        NormalizedMood.NEUTRAL -> "😐"
        NormalizedMood.STRESSED -> "😓"
        NormalizedMood.ANXIOUS -> "😰"
        NormalizedMood.SAD -> "😢"
        NormalizedMood.DEPRESSED -> "😞"
        NormalizedMood.ANGRY -> "😠"
        NormalizedMood.OVERWHELMED -> "😵"
    }
}

fun formatMoodName(mood: NormalizedMood): String {
    return mood.name
        .split('_')
        .joinToString(" ") { word ->
            word.lowercase().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }
        }
}
