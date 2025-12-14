package org.example.project.ui.mood_result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.NormalizedMood

/**
 * SCREEN 4: YOUR REFLECTION
 * Journal text with typing animation
 * - Abstract background
 * - Content container for easy reading
 * - Typing effect
 * - Dark text for clarity
 */
@Composable
fun Screen4_YourReflection(
    mood: NormalizedMood,
    journalText: String,
    isVisible: Boolean = true
) {
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
            CalmSurface(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .heightIn(max = 550.dp)
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title (dark text for strong contrast)
                    Text(
                        text = "Your Reflection",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937), // Near-black
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 28.dp)
                    )

                    // Journal content with typing animation and scroll indicator
                    val scrollState = rememberScrollState()
                    val canScrollDown by remember {
                        derivedStateOf {
                            scrollState.value < scrollState.maxValue
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(scrollState)
                        ) {
                            TypewriterText(
                                text = journalText,
                                fontSize = 18.sp,
                                color = Color(0xFF475569), // Medium gray for body text
                                textAlign = TextAlign.Center,
                                delayMs = 40L,
                                isVisible = isVisible
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
