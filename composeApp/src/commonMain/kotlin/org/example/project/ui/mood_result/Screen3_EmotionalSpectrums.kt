package org.example.project.ui.mood_result

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun Screen3_EmotionalSpectrums(
    mood: NormalizedMood,
    emotion: MoodEmotionScore
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MoodAbstractBackground(mood)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "How you're feeling",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.95f),
                modifier = Modifier.padding(bottom = 48.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
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
                    markerColor = Color(0xFF10B981)
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
                    markerColor = Color(0xFFF59E0B)
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
                    markerColor = if (emotion.stress > 0.6f) Color(0xFFF97316) else Color(0xFF10B981)
                )
            }
        }
    }
}

@Composable
private fun EmotionalSpectrum(
    label: String,
    leftLabel: String,
    rightLabel: String,
    position: Float,
    gradient: Brush,
    markerColor: Color
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.90f),
            letterSpacing = 0.5.sp
        )

        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            val maxWidthPx = maxWidth
            val markerSize = 24.dp
            val availableWidth = maxWidthPx - markerSize
            val markerOffset = availableWidth * position.coerceIn(0f, 1f)

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
                color = Color.White.copy(alpha = 0.75f)
            )
            Text(
                text = rightLabel,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.75f)
            )
        }
    }
}
