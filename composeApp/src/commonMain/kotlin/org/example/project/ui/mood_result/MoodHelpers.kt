package org.example.project.ui.mood_result

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import model.MoodEmotionScore
import model.NormalizedMood
import kotlin.math.PI
import kotlin.math.sin

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

fun extractMoodBackgroundColor(mood: NormalizedMood): Color {
    val baseColor = getMoodColor(mood)
    return baseColor.copy(alpha = 0.06f)
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

@Composable
fun MoodAbstractBackground(mood: NormalizedMood, modifier: Modifier = Modifier) {
    val baseColor = getMoodColor(mood)
    val lightColor = baseColor.copy(alpha = 0.15f)
    val mediumColor = baseColor.copy(alpha = 0.25f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White,
                        lightColor,
                        mediumColor
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1500f)
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawSoftBlob(
                centerX = size.width * 0.2f,
                centerY = size.height * 0.15f,
                radius = size.width * 0.4f,
                color = baseColor.copy(alpha = 0.08f)
            )

            drawWave(
                startY = size.height * 0.4f,
                amplitude = size.height * 0.08f,
                wavelength = size.width * 0.6f,
                color = baseColor.copy(alpha = 0.10f)
            )

            drawSoftBlob(
                centerX = size.width * 0.75f,
                centerY = size.height * 0.7f,
                radius = size.width * 0.25f,
                color = lightColor.copy(alpha = 0.5f)
            )

            drawGentleArc(
                startX = size.width * 0.6f,
                startY = size.height * 0.05f,
                endX = size.width * 0.95f,
                endY = size.height * 0.25f,
                controlOffset = size.width * 0.15f,
                color = baseColor.copy(alpha = 0.06f)
            )

            drawSoftBlob(
                centerX = size.width * 0.15f,
                centerY = size.height * 0.85f,
                radius = size.width * 0.3f,
                color = baseColor.copy(alpha = 0.09f)
            )
        }
    }
}

private fun DrawScope.drawSoftBlob(
    centerX: Float,
    centerY: Float,
    radius: Float,
    color: Color
) {
    val path = Path().apply {
        val points = 60
        val angleStep = (2 * PI) / points

        for (i in 0..points) {
            val angle = i * angleStep
            val radiusVariation = radius + (radius * 0.15f * sin(angle * 3).toFloat())
            val x = centerX + (radiusVariation * kotlin.math.cos(angle)).toFloat()
            val y = centerY + (radiusVariation * sin(angle)).toFloat()

            if (i == 0) {
                moveTo(x, y)
            } else {
                lineTo(x, y)
            }
        }
        close()
    }

    drawPath(path = path, color = color)
}

private fun DrawScope.drawWave(
    startY: Float,
    amplitude: Float,
    wavelength: Float,
    color: Color
) {
    val path = Path().apply {
        val steps = 100
        val stepSize = size.width / steps

        moveTo(0f, startY)

        for (i in 0..steps) {
            val x = i * stepSize
            val y = startY + amplitude * sin(2 * PI * x / wavelength).toFloat()
            lineTo(x, y)
        }

        lineTo(size.width, size.height)
        lineTo(0f, size.height)
        close()
    }

    drawPath(path = path, color = color)
}

private fun DrawScope.drawGentleArc(
    startX: Float,
    startY: Float,
    endX: Float,
    endY: Float,
    controlOffset: Float,
    color: Color
) {
    val path = Path().apply {
        moveTo(startX, startY)

        val controlX = (startX + endX) / 2 + controlOffset
        val controlY = (startY + endY) / 2

        quadraticTo(controlX, controlY, endX, endY)
        lineTo(endX, endY + 80)
        quadraticTo(controlX, controlY + 80, startX, startY + 80)
        close()
    }

    drawPath(path = path, color = color)
}
