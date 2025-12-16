package org.example.project.ui.mood_result

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import model.NormalizedMood

/**
 * SCREEN 4: YOUR REFLECTION
 * Journal text with typing animation
 * - Abstract background
 * - Content container for easy reading
 * - Typing effect
 * - Dark text for clarity
 * - Animated entrance
 */
@Composable
fun Screen4_YourReflection(
    mood: NormalizedMood,
    journalText: String,
    isVisible: Boolean = true
) {
    var showContainer by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(100)
            showContainer = true
            delay(300)
            showTitle = true
            delay(400)
            showContent = true
        } else {
            showContainer = false
            showTitle = false
            showContent = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Abstract background
        MoodAbstractBackground(mood)

        // Centered content with container
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = showContainer,
                enter = scaleIn(
                    initialScale = 0.9f,
                    animationSpec = tween(600, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(600))
            ) {
                CalmSurface(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .heightIn(max = 550.dp)
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Animated title with icon
                        AnimatedVisibility(
                            visible = showTitle,
                            enter = slideInVertically(
                                initialOffsetY = { -it / 2 },
                                animationSpec = tween(500, easing = FastOutSlowInEasing)
                            ) + fadeIn(animationSpec = tween(500))
                        ) {
                            Row(
                                modifier = Modifier.padding(bottom = 28.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "✨",
                                    fontSize = 28.sp,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = "Your Reflection",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F2937),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Journal content with typing animation and scroll indicator
                        val scrollState = rememberScrollState()
                        val canScrollDown by remember {
                            derivedStateOf {
                                scrollState.value < scrollState.maxValue
                            }
                        }

                        AnimatedVisibility(
                            visible = showContent,
                            enter = fadeIn(animationSpec = tween(800))
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .verticalScroll(scrollState)
                                ) {
                                    TypewriterText(
                                        text = journalText,
                                        fontSize = 18.sp,
                                        color = Color(0xFF475569),
                                        textAlign = TextAlign.Center,
                                        delayMs = 40L,
                                        isVisible = showContent
                                    )
                                }

                                // Scroll indicator gradient at bottom
                                if (canScrollDown) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(60.dp)
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
}
