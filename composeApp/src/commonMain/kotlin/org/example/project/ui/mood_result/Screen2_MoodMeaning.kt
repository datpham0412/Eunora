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
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Brush

@Composable
fun Screen2_MoodMeaning(
    mood: NormalizedMood,
    emotion: MoodEmotionScore,
    isVisible: Boolean = true
) {
    var showEmoji by remember { mutableStateOf(false) }
    var showMoodName by remember { mutableStateOf(false) }
    var showSummary by remember { mutableStateOf(false) }

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
                    modifier = Modifier
                        .heightIn(max = 600.dp)
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                    AnimatedVisibility(
                        visible = showMoodName,
                        enter = slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = tween(600, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(600))
                    ) {
                        val formattedName = formatMoodName(mood)
                        val fontSize = when {
                            formattedName.length > 15 -> 26.sp
                            formattedName.length > 12 -> 28.sp
                            else -> 32.sp
                        }

                        Text(
                            text = formattedName,
                            fontSize = fontSize,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 24.dp),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                    val scrollState = rememberScrollState()
                    val canScrollDown by remember {
                        derivedStateOf {
                            scrollState.value < scrollState.maxValue
                        }
                    }

                    AnimatedVisibility(
                        visible = showSummary,
                        enter = fadeIn(animationSpec = tween(800))
                    ) {
                         Box(modifier = Modifier.fillMaxWidth().weight(1f, fill = false)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(scrollState)
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
                            
                            if (canScrollDown) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .align(Alignment.BottomCenter)
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(
                                                    Color.Transparent,
                                                    Color.White
                                                )
                                            )
                                        )
                                )
                            }
                        }
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
    fontWeight: FontWeight = FontWeight.Normal,
    lineHeight: androidx.compose.ui.unit.TextUnit = 32.sp,
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
        fontWeight = fontWeight,
        color = color,
        textAlign = textAlign,
        lineHeight = lineHeight,
        modifier = modifier
    )
}
