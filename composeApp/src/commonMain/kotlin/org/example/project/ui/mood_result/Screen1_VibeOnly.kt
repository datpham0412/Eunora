package org.example.project.ui.mood_result

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import model.NormalizedMood
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.material3.Surface
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Screen1_VibeOnly(
    mood: NormalizedMood,
    artAssetId: String?,
    getMoodArtworkResource: (NormalizedMood, String?) -> DrawableResource?
) {
    var showContent by remember { mutableStateOf(false) }

    // Breathing animation for background
    val infiniteTransition = rememberInfiniteTransition()
    val breathingScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(Unit) {
        delay(200)
        showContent = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background with gentle breathing effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = breathingScale
                    scaleY = breathingScale
                }
        ) {
            MoodAbstractBackground(mood)
        }

        // Main Content: Image + Text
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val imageResource = getMoodArtworkResource(mood, artAssetId)
            if (imageResource != null) {
                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn(animationSpec = tween(1200)) + 
                            scaleIn(initialScale = 0.9f, animationSpec = tween(1500, easing = LinearOutSlowInEasing))
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .aspectRatio(1f), // Keep it square
                        shape = RoundedCornerShape(32.dp),
                        shadowElevation = 0.dp,
                        color = Color.White
                    ) {
                        Image(
                            painter = painterResource(imageResource),
                            contentDescription = "Mood atmosphere",
                            contentScale = ContentScale.Crop, // Fill the square card
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))

            // Animated mood name reveal
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(
                    animationSpec = tween(800, delayMillis = 300, easing = FastOutSlowInEasing)
                ) + scaleIn(
                    initialScale = 0.7f,
                    animationSpec = tween(800, delayMillis = 300, easing = FastOutSlowInEasing)
                )
            ) {
                MoodNameText(mood = mood)
            }
        }
    }
}

@Composable
private fun MoodNameText(
    mood: NormalizedMood,
    modifier: Modifier = Modifier
) {
    // Split mood name into words (e.g., "CALM_POSITIVE" -> ["calm", "positive"])
    val words = mood.name
        .split('_')
        .map { it.lowercase() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        words.forEach { word ->
            // Adjust font size for long words to prevent overflow
            val fontSize = when {
                word.length > 10 -> 46.sp  // "overwhelmed" (11 chars)
                word.length > 8 -> 52.sp   // "energetic" (9 chars)
                else -> 58.sp              // Normal size
            }

            val letterSpacing = when {
                word.length > 10 -> 4.sp   // Reduce letter spacing for very long words
                word.length > 8 -> 5.sp
                else -> 6.sp
            }

            Text(
                text = word,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.95f),
                textAlign = TextAlign.Center,
                letterSpacing = letterSpacing,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                softWrap = false
            )
        }
    }
}
