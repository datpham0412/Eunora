package org.example.project.ui.mood_result

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.MoodEmotionScore
import model.NormalizedMood

/**
 * SCREEN 3: EMOTIONAL SPECTRUMS
 * Visual-only emotion spectrums
 * - Abstract background
 * - Content container for readability
 * - Dark text labels
 * - No percentages
 */
@Composable
fun Screen3_EmotionalSpectrums(
    mood: NormalizedMood,
    emotion: MoodEmotionScore,
    isVisible: Boolean = true
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MoodAbstractBackground(mood)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CalmSurface(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(40.dp),
                    verticalArrangement = Arrangement.spacedBy(36.dp)
                ) {
                    Text(
                        text = "How you're feeling",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937) // Near-black
                    )

                    EmotionalSpectrum(
                        label = "Emotional tone",
                        leftLabel = "Difficult",
                        rightLabel = "Positive",
                        position = emotion.positivity,
                        gradient = Brush.horizontalGradient(
                            listOf(
                                Color(0xFF9CA3AF).copy(alpha = 0.3f),
                                Color(0xFF10B981).copy(alpha = 0.3f)
                            )
                        ),
                        markerColor = Color(0xFF10B981),
                        isVisible = isVisible
                    )

                    EmotionalSpectrum(
                        label = "Energy level",
                        leftLabel = "Resting",
                        rightLabel = "Energized",
                        position = emotion.energy,
                        gradient = Brush.horizontalGradient(
                            listOf(
                                Color(0xFFA78BFA).copy(alpha = 0.3f),
                                Color(0xFFF59E0B).copy(alpha = 0.3f)
                            )
                        ),
                        markerColor = Color(0xFFF59E0B),
                        isVisible = isVisible
                    )

                    EmotionalSpectrum(
                        label = "Inner state",
                        leftLabel = "At ease",
                        rightLabel = "Tense",
                        position = emotion.stress,
                        gradient = Brush.horizontalGradient(
                            listOf(
                                Color(0xFF10B981).copy(alpha = 0.3f),
                                Color(0xFFF97316).copy(alpha = 0.3f)
                            )
                        ),
                        markerColor = if (emotion.stress > 0.6f) Color(0xFFF97316) else Color(0xFF10B981),
                        isVisible = isVisible
                    )
                }
            }
        }
    }
}

@Composable
internal fun EmotionalSpectrum(
    label: String,
    leftLabel: String,
    rightLabel: String,
    position: Float,
    gradient: Brush,
    markerColor: Color,
    isVisible: Boolean = true
) {
    val animatedPosition by animateFloatAsState(
        targetValue = if (isVisible) position else 0.5f,
        animationSpec = tween(durationMillis = 1200),
        label = "markerPosition"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF334155),
            letterSpacing = 0.5.sp
        )

        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            val maxWidthPx = maxWidth
            val markerSize = 24.dp
            val availableWidth = maxWidthPx - markerSize
            val markerOffset = availableWidth * animatedPosition.coerceIn(0f, 1f)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(gradient)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = markerOffset)
                    .size(markerSize)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(
                        width = 4.dp,
                        color = markerColor,
                        shape = CircleShape
                    )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = leftLabel,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF64748B)
            )
            Text(
                text = rightLabel,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF64748B)
            )
        }
    }
}
