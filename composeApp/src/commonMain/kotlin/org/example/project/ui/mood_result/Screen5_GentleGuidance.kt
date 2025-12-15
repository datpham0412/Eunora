package org.example.project.ui.mood_result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.NormalizedMood

/**
 * SCREEN 5: GENTLE GUIDANCE
 * Advice + CTA button
 * - Abstract background
 * - Content container for readability
 * - Typing animation
 * - Prominent CTA button
 */
@Composable
fun Screen5_GentleGuidance(
    mood: NormalizedMood,
    adviceText: String,
    onNewMood: () -> Unit,
    isVisible: Boolean = true
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Abstract background
        MoodAbstractBackground(mood)

        // Centered content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .padding(vertical = 40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Advice section with container
            CalmSurface(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .heightIn(max = 550.dp)
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title with icon
                    Row(
                        modifier = Modifier.padding(bottom = 28.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💡",
                            fontSize = 28.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "Gentle Guidance",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937), // Near-black
                            textAlign = TextAlign.Center
                        )
                    }

                    // Advice with typing animation and scroll indicator
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
                                text = adviceText,
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

            Spacer(modifier = Modifier.height(24.dp))

            // CTA button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF9470F4),
                                Color(0xFF5E82F3),
                                Color(0xFFD770C5)
                            )
                        )
                    )
            ) {
                Button(
                    onClick = onNewMood,
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
                ) {
                    Text(
                        "New Reflection",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
