package org.example.project.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.MoodEmotionScore
import model.MoodEntry
import model.NormalizedMood
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ai_mood_journal.composeapp.generated.resources.Res
import ai_mood_journal.composeapp.generated.resources.mood_art_calm_positive_1

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // HERO SECTION - Pure mood artwork, no text
            MoodHeroSection(
                mood = moodEntry.normalizedMood,
                artAssetId = moodEntry.art.assetId
            )

            // Content cards with padding
            Column(
                modifier = Modifier
                    .padding(horizontal = 28.dp)
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)  // Increased spacing
            ) {
                // NEW: Mood identity + reflective summary
                MoodIdentityCard(
                    mood = moodEntry.normalizedMood,
                    emotion = moodEntry.ai.emotion
                )

                // NEW: Visual-only emotional spectrum
                EmotionalSpectrumCard(
                    emotion = moodEntry.ai.emotion
                )

                // Journal (renamed)
                SectionCard(
                    title = "Your Reflection",
                    content = moodEntry.ai.journal,
                    icon = "📝"
                )

                // Advice (renamed)
                SectionCard(
                    title = "Gentle Guidance",
                    content = moodEntry.ai.advice,
                    icon = "💡"
                )

                // Action button (updated text)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF9470F4),
                                    Color(0xFF5E82F3),
                                    Color(0xFFD770C5)
                                )
                            )
                        )
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
                            "New Reflection",
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
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MoodArtworkImage(
    mood: NormalizedMood,
    artAssetId: String?
) {
    val imageResource = getMoodArtworkResource(mood, artAssetId)

    if (imageResource != null) {
        Image(
            painter = painterResource(imageResource),
            contentDescription = "Mood atmosphere for ${formatMoodName(mood)}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        // Fallback: Color gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            getMoodColor(mood).copy(alpha = 0.4f),
                            getMoodColor(mood).copy(alpha = 0.6f)
                        )
                    )
                )
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MoodHeroSection(
    mood: NormalizedMood,
    artAssetId: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.35f)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
    ) {
        // Layer 1: Mood artwork
        MoodArtworkImage(mood = mood, artAssetId = artAssetId)

        // Layer 2: Very subtle gradient overlay (for depth, not text contrast)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.15f)  // Much more subtle
                        )
                    )
                )
        )

        // NO TEXT OVERLAY - Pure artwork only
    }
}

// NEW COMPONENT: Mood Identity Card
@Composable
fun MoodIdentityCard(
    mood: NormalizedMood,
    emotion: MoodEmotionScore
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 1.dp  // Reduced from 2dp
    ) {
        Box(
            modifier = Modifier.background(
                Brush.verticalGradient(
                    listOf(
                        Color.White,
                        Color(0xFFFAF5FF).copy(alpha = 0.3f)
                    )
                )
            )
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Mood emoji (large, centered)
                Text(
                    text = getMoodEmoji(mood),
                    fontSize = 56.sp
                )

                // Mood name (soft, not bold)
                Text(
                    text = formatMoodName(mood),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 22.sp
                    ),
                    fontWeight = FontWeight.Medium,  // Not Bold
                    color = Color(0xFF1F2937),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Reflective summary
                Text(
                    text = getReflectiveSummary(emotion),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    ),
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF4A5568).copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// NEW COMPONENT: Emotional Spectrum Card
@Composable
fun EmotionalSpectrumCard(emotion: MoodEmotionScore) {
    var showDetails by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Box(
            modifier = Modifier.background(
                Brush.verticalGradient(
                    listOf(
                        Color.White,
                        Color(0xFFFAF5FF).copy(alpha = 0.3f)
                    )
                )
            )
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Soft heading
                Text(
                    text = "How you're feeling right now",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 17.sp
                    ),
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1F2937).copy(alpha = 0.85f)
                )

                // Spectrum 1: Emotional Tone
                EmotionalSpectrum(
                    label = "Emotional tone",
                    leftLabel = "Difficult",
                    rightLabel = "Positive",
                    position = emotion.positivity,
                    gradient = Brush.horizontalGradient(
                        listOf(
                            Color(0xFF9CA3AF).copy(alpha = 0.25f),
                            Color(0xFF10B981).copy(alpha = 0.25f)
                        )
                    ),
                    markerColor = Color(0xFF10B981)
                )

                // Spectrum 2: Energy Level
                EmotionalSpectrum(
                    label = "Energy level",
                    leftLabel = "Resting",
                    rightLabel = "Energized",
                    position = emotion.energy,
                    gradient = Brush.horizontalGradient(
                        listOf(
                            Color(0xFFA78BFA).copy(alpha = 0.25f),
                            Color(0xFFF59E0B).copy(alpha = 0.25f)
                        )
                    ),
                    markerColor = Color(0xFFF59E0B)
                )

                // Spectrum 3: Inner State
                EmotionalSpectrum(
                    label = "Inner state",
                    leftLabel = "At ease",
                    rightLabel = "Tense",
                    position = emotion.stress,
                    gradient = Brush.horizontalGradient(
                        listOf(
                            Color(0xFF10B981).copy(alpha = 0.25f),
                            Color(0xFFF97316).copy(alpha = 0.25f)
                        )
                    ),
                    markerColor = if (emotion.stress > 0.6f) Color(0xFFF97316) else Color(0xFF10B981)
                )

                // Optional: Expandable details
                TextButton(
                    onClick = { showDetails = !showDetails },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = if (showDetails) "Hide details" else "Show detailed scores",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280).copy(alpha = 0.7f)
                    )
                }

                if (showDetails) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFFF9FAFB),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Detailed values",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF6B7280)
                        )
                        DetailScoreRow("Positivity", emotion.positivity)
                        DetailScoreRow("Energy", emotion.energy)
                        DetailScoreRow("Stress", emotion.stress)
                    }
                }
            }
        }
    }
}

// NEW COMPONENT: Individual Emotional Spectrum
@Composable
fun EmotionalSpectrum(
    label: String,
    leftLabel: String,
    rightLabel: String,
    position: Float,
    gradient: Brush,
    markerColor: Color
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Soft label
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF6B7280),
            letterSpacing = 0.2.sp
        )

        // Spectrum bar
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            val maxWidthPx = maxWidth
            val markerSize = 20.dp
            val availableWidth = maxWidthPx - markerSize
            val markerOffset = availableWidth * position.coerceIn(0f, 1f)

            // Background gradient bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(gradient)
            )

            // Position marker (soft circle)
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = markerOffset)
                    .size(markerSize)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(
                        width = 3.dp,
                        color = markerColor,
                        shape = CircleShape
                    )
            )
        }

        // Spectrum labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = leftLabel,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF6B7280).copy(alpha = 0.7f)
            )
            Text(
                text = rightLabel,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF6B7280).copy(alpha = 0.7f)
            )
        }
    }
}

// NEW COMPONENT: Detail Score Row (minimal)
@Composable
fun DetailScoreRow(label: String, score: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color(0xFF6B7280)
        )
        Text(
            text = "${(score * 100).toInt()}%",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF4B5563)
        )
    }
}

@Composable
fun SectionCard(title: String, content: String, icon: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 1.dp  // Reduced from 2dp
    ) {
        Box(
            modifier = Modifier.background(
                Brush.verticalGradient(
                    listOf(
                        Color.White,
                        Color(0xFFFAF5FF).copy(alpha = 0.3f)
                    )
                )
            )
        ) {
            Column(
                modifier = Modifier.padding(28.dp),  // Increased from 24dp
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "$icon $title",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 19.sp
                    ),
                    fontWeight = FontWeight.Medium,  // Reduced from SemiBold
                    color = Color(0xFF1F2937).copy(alpha = 0.85f)  // Softer
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

// Helper functions for mood colors and emojis
fun getMoodColor(mood: NormalizedMood): Color {
    return when (mood) {
        NormalizedMood.CALM_POSITIVE -> Color(0xFF10B981)
        NormalizedMood.HAPPY_ENERGETIC -> Color(0xFFF59E0B)
        NormalizedMood.EXCITED -> Color(0xFFEC4899)
        NormalizedMood.NEUTRAL -> Color(0xFF6B7280)
        NormalizedMood.STRESSED -> Color(0xFFF97316)
        NormalizedMood.ANXIOUS -> Color(0xFF8B5CF6)
        NormalizedMood.SAD -> Color(0xFF3B82F6)
        NormalizedMood.DEPRESSED -> Color(0xFF6366F1)
        NormalizedMood.ANGRY -> Color(0xFFEF4444)
        NormalizedMood.OVERWHELMED -> Color(0xFFDC2626)
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

// NEW: Reflective summary (softer, present-tense, non-judgmental)
fun getReflectiveSummary(emotion: MoodEmotionScore): String {
    val tone = when {
        emotion.positivity > 0.7f -> "feeling emotionally positive"
        emotion.positivity > 0.4f -> "in a balanced place"
        else -> "holding some difficult feelings"
    }

    val energy = when {
        emotion.energy > 0.7f -> "energized"
        emotion.energy > 0.4f -> "gently active"
        else -> "in a resting state"
    }

    val inner = when {
        emotion.stress > 0.7f -> "with some tension present"
        emotion.stress > 0.4f -> "with a bit of pressure"
        else -> "feeling at ease"
    }

    return "You're $tone, $energy, $inner."
}

// Maps mood to generated DrawableResource
@OptIn(ExperimentalResourceApi::class)
fun getMoodArtworkResource(mood: NormalizedMood, assetId: String?) =
    when (mood) {
        NormalizedMood.CALM_POSITIVE -> Res.drawable.mood_art_calm_positive_1
        // Add more moods as you add their images:
        // NormalizedMood.HAPPY_ENERGETIC -> Res.drawable.mood_art_happy_energetic_1
        // NormalizedMood.EXCITED -> Res.drawable.mood_art_excited_1
        // ... etc
        else -> null // Fallback to gradient for moods without images
    }

// Asset path helper (for documentation purposes)
fun getMoodArtAssetPath(mood: NormalizedMood, assetId: String?): String {
    val moodName = mood.name.lowercase()
    val imageIndex = assetId?.toIntOrNull() ?: 1
    return "mood_art_${moodName}_${imageIndex}.png"
}
