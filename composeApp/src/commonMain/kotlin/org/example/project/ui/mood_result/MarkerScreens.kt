package org.example.project.ui.mood_result

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import model.NormalizedMood

/**
 * PAUSE MARKER - HIGH ACTIVATION
 * 3-second forced pause with no interaction
 */
@Composable
fun PauseMarker(
    mood: NormalizedMood,
    onComplete: () -> Unit
) {
    val baseColor = getMoodColor(mood)
    val backgroundColor = extractMoodBackgroundColor(mood)

    var showContainer by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        showContainer = true
        delay(300)
        showText = true
        delay(2600) // Total 3 seconds
        onComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Abstract background matching mood theme
        MoodAbstractBackground(mood)

        // Centered content with entrance animation
        AnimatedVisibility(
            visible = showContainer,
            enter = fadeIn(animationSpec = tween(600, easing = FastOutSlowInEasing)) +
                    slideInVertically(
                        initialOffsetY = { it / 8 },
                        animationSpec = tween(600, easing = FastOutSlowInEasing)
                    )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CalmSurface(
                    modifier = Modifier.padding(horizontal = 48.dp)
                ) {
                    AnimatedVisibility(
                        visible = showText,
                        enter = fadeIn(animationSpec = tween(500))
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(40.dp)
                        ) {
                            Text(
                                text = "Strong emotions don't need quick answers.",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1F2937),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Let's pause for a moment.",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF374151),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * PERMISSION MARKER - LOW ENERGY
 * Choice to continue or end the flow
 */
@Composable
fun PermissionMarker(
    mood: NormalizedMood,
    onContinue: () -> Unit,
    onEnd: () -> Unit
) {
    val baseColor = getMoodColor(mood)
    val backgroundColor = extractMoodBackgroundColor(mood)

    var showContainer by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }
    var showSubline by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        showContainer = true
        delay(300)
        showTitle = true
        delay(400)
        showSubline = true
        delay(300)
        showButtons = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Abstract background matching mood theme
        MoodAbstractBackground(mood)

        // Centered content with entrance animation
        AnimatedVisibility(
            visible = showContainer,
            enter = fadeIn(animationSpec = tween(600, easing = FastOutSlowInEasing)) +
                    slideInVertically(
                        initialOffsetY = { it / 8 },
                        animationSpec = tween(600, easing = FastOutSlowInEasing)
                    )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CalmSurface(
                    modifier = Modifier.padding(horizontal = 48.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.padding(40.dp)
                    ) {
                        // Title
                        AnimatedVisibility(
                            visible = showTitle,
                            enter = fadeIn(animationSpec = tween(500))
                        ) {
                            Text(
                                text = "You don't have to go further.",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F2937),
                                textAlign = TextAlign.Center
                            )
                        }

                        // Subline
                        AnimatedVisibility(
                            visible = showSubline,
                            enter = fadeIn(animationSpec = tween(500))
                        ) {
                            Text(
                                text = "You can end this check-in now, or continue when you feel ready.",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF4B5563),
                                textAlign = TextAlign.Center,
                                lineHeight = 26.sp
                            )
                        }

                        // Buttons
                        AnimatedVisibility(
                            visible = showButtons,
                            enter = fadeIn(animationSpec = tween(500))
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // End button
                                Button(
                                    onClick = onEnd,
                                    modifier = Modifier
                                        .height(56.dp)
                                        .weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = baseColor.copy(alpha = 0.15f)
                                    ),
                                    shape = RoundedCornerShape(28.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "End here",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = baseColor.copy(alpha = 0.8f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                // Continue button
                                Button(
                                    onClick = onContinue,
                                    modifier = Modifier
                                        .height(56.dp)
                                        .weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = baseColor.copy(alpha = 0.85f)
                                    ),
                                    shape = RoundedCornerShape(28.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Continue",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = TextAlign.Center
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
}

/**
 * HIGHLIGHT MARKER - POSITIVE
 * Optional text input to capture moment
 */
@Composable
fun HighlightMarker(
    mood: NormalizedMood,
    onComplete: (String?) -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val baseColor = getMoodColor(mood)
    val backgroundColor = extractMoodBackgroundColor(mood)

    var showContainer by remember { mutableStateOf(false) }
    var showPrompt by remember { mutableStateOf(false) }
    var showInput by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        showContainer = true
        delay(300)
        showPrompt = true
        delay(400)
        showInput = true
        delay(300)
        showButton = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        // Dismiss keyboard when tapping outside
                        focusManager.clearFocus()
                    }
                )
            }
    ) {
        // Abstract background matching mood theme
        MoodAbstractBackground(mood)

        // Centered content with entrance animation
        AnimatedVisibility(
            visible = showContainer,
            enter = fadeIn(animationSpec = tween(600, easing = FastOutSlowInEasing)) +
                    slideInVertically(
                        initialOffsetY = { it / 8 },
                        animationSpec = tween(600, easing = FastOutSlowInEasing)
                    )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CalmSurface(
                    modifier = Modifier.padding(horizontal = 48.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.padding(40.dp)
                    ) {
                        // Prompt text (standard color)
                        AnimatedVisibility(
                            visible = showPrompt,
                            enter = fadeIn(animationSpec = tween(500))
                        ) {
                            Text(
                                text = "What's one small moment you'd like to keep?",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1F2937), // Standard text color
                                textAlign = TextAlign.Center
                            )
                        }

                        // Text input
                        AnimatedVisibility(
                            visible = showInput,
                            enter = fadeIn(animationSpec = tween(500))
                        ) {
                            BasicTextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(baseColor.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                                    .padding(16.dp),
                                textStyle = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color(0xFF1F2937), // Standard text color
                                    textAlign = TextAlign.Center
                                ),
                                cursorBrush = SolidColor(baseColor),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (inputText.isEmpty()) {
                                            Text(
                                                text = "A word, a feeling, or a moment",
                                                fontSize = 18.sp,
                                                color = Color(0xFF9CA3AF), // Standard placeholder color
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )
                        }

                        // Continue button
                        AnimatedVisibility(
                            visible = showButton,
                            enter = fadeIn(animationSpec = tween(500))
                        ) {
                            Button(
                                onClick = {
                                    focusManager.clearFocus()
                                    onComplete(inputText.ifBlank { null })
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = baseColor.copy(alpha = 0.85f)
                                ),
                                shape = RoundedCornerShape(28.dp)
                            ) {
                                Text(
                                    "Continue",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
