package org.example.project.ui.mood_result

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import model.MoodEmotionScore
import model.NormalizedMood

/**
 * SCREEN 2: MOOD MEANING
 * Emoji + mood name + reflective summary
 * - Abstract background
 * - Soft content container for readability
 * - Typing animation effect
 * - Dark text inside container
 */
@Composable
fun Screen2_MoodMeaning(
    mood: NormalizedMood,
    emotion: MoodEmotionScore,
    isVisible: Boolean = true
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Abstract background
        MoodAbstractBackground(mood)

        // Centered content with calm surface container
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Large emoji
                    Text(
                        text = getMoodEmoji(mood),
                        fontSize = 80.sp,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    // Mood name (dark text for readability)
                    Text(
                        text = formatMoodName(mood),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937), // Near-black for strong contrast
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Reflective summary with typing animation
                    TypewriterText(
                        text = getReflectiveSummary(emotion),
                        fontSize = 20.sp,
                        color = Color(0xFF475569), // Medium gray for body text
                        textAlign = TextAlign.Center,
                        isVisible = isVisible
                    )
                }
            }
        }
    }
}

/**
 * Typewriter effect composable
 * Reveals text character by character
 */
@Composable
fun TypewriterText(
    text: String,
    fontSize: androidx.compose.ui.unit.TextUnit,
    color: Color,
    textAlign: TextAlign,
    modifier: Modifier = Modifier,
    delayMs: Long = 60L,
    isVisible: Boolean = true
) {
    var visibleText by remember { mutableStateOf("") }

    LaunchedEffect(text, isVisible) {
        if (isVisible) {
            visibleText = ""
            text.forEachIndexed { index, _ ->
                delay(delayMs)
                visibleText = text.substring(0, index + 1)
            }
        }
    }

    Text(
        text = visibleText,
        fontSize = fontSize,
        fontWeight = FontWeight.Normal,
        color = color,
        textAlign = textAlign,
        lineHeight = 32.sp,
        modifier = modifier
    )
}
