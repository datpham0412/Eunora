package org.example.project.ui.mood_result

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
 * - Staggered entrance animations
 */
@Composable
fun Screen2_MoodMeaning(
    mood: NormalizedMood,
    emotion: MoodEmotionScore,
    isVisible: Boolean = true
) {
    var showEmoji by remember { mutableStateOf(false) }
    var showMoodName by remember { mutableStateOf(false) }
    var showSummary by remember { mutableStateOf(false) }

    // Gentle floating animation for emoji
    val infiniteTransition = rememberInfiniteTransition()
    val emojiFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(200)
            showEmoji = true
            delay(300)
            showMoodName = true
            delay(400)
            showSummary = true
        } else {
            showEmoji = false
            showMoodName = false
            showSummary = false
        }
    }

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
                    // Animated emoji with floating effect
                    AnimatedVisibility(
                        visible = showEmoji,
                        enter = scaleIn(
                            initialScale = 0.3f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ) + fadeIn(animationSpec = tween(500))
                    ) {
                        Text(
                            text = getMoodEmoji(mood),
                            fontSize = 80.sp,
                            modifier = Modifier
                                .padding(bottom = 32.dp)
                                .graphicsLayer {
                                    translationY = emojiFloat
                                }
                        )
                    }

                    // Mood name with slide up animation
                    AnimatedVisibility(
                        visible = showMoodName,
                        enter = slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = tween(600, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(600))
                    ) {
                        Text(
                            text = formatMoodName(mood),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }

                    // Reflective summary with typing animation
                    AnimatedVisibility(
                        visible = showSummary,
                        enter = fadeIn(animationSpec = tween(800))
                    ) {
                        TypewriterText(
                            text = getReflectiveSummary(emotion),
                            fontSize = 20.sp,
                            color = Color(0xFF475569),
                            textAlign = TextAlign.Center,
                            delayMs = 40L,
                            isVisible = showSummary
                        )
                    }
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
    delayMs: Long = 40L,
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
