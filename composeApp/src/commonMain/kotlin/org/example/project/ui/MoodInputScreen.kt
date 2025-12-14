package org.example.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodInputScreen(
    userInput: String,
    isLoading: Boolean,
    error: String?,
    onInputChange: (String) -> Unit,
    onAnalyze: () -> Unit,
    onClearError: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFCDB8F0),
            Color(0xFFB8C8F5),
            Color(0xFFE8C5E6),
            Color(0xFFC8CDF3)
        )
    )

    val buttonGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF9470F4),
            Color(0xFF5E82F3),
            Color(0xFFD770C5)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "How are you feeling?",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 34.sp,
                    lineHeight = 42.sp
                ),
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937),
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Let your thoughts flow freely",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 17.sp,
                    lineHeight = 24.sp
                ),
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4A5568).copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(56.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 240.dp),
                shape = RoundedCornerShape(32.dp),
                color = Color.White,
                tonalElevation = 0.dp,
                shadowElevation = 3.dp
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.White,
                                    Color(0xFFFAF5FF)
                                )
                            )
                        )
                        .padding(28.dp)
                ) {
                    BasicTextField(
                        value = userInput,
                        onValueChange = onInputChange,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            fontSize = 17.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF1F2937),
                            letterSpacing = 0.3.sp
                        ),
                        enabled = !isLoading,
                        decorationBox = { innerTextField ->
                            if (userInput.isEmpty()) {
                                Text(
                                    text = "I feel...",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 17.sp,
                                        lineHeight = 28.sp,
                                        letterSpacing = 0.3.sp
                                    ),
                                    fontWeight = FontWeight.Light,
                                    color = Color(0xFF4A5568).copy(alpha = 0.4f)
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }

            if (error != null) {
                Spacer(modifier = Modifier.height(20.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFFFED7D7).copy(alpha = 0.6f),
                    tonalElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            ),
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF742A2A).copy(alpha = 0.8f),
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = onClearError) {
                            Text(
                                "Dismiss",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF742A2A).copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(
                        if (!isLoading && userInput.isNotBlank()) {
                            buttonGradient
                        } else {
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFFCDC8F0).copy(alpha = 0.7f),
                                    Color(0xFFC8CFF5).copy(alpha = 0.7f)
                                )
                            )
                        }
                    )
            ) {
                Button(
                    onClick = onAnalyze,
                    modifier = Modifier.fillMaxSize(),
                    enabled = !isLoading && userInput.isNotBlank(),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            "Understanding your mood...",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                letterSpacing = 0.3.sp
                            ),
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    } else {
                        Text(
                            "Reflect",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 17.sp,
                                letterSpacing = 0.5.sp
                            ),
                            fontWeight = FontWeight.Medium,
                            color = if (userInput.isNotBlank()) Color.White else Color(0xFF718096).copy(alpha = 0.5f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Emoji mood selector
            val moods = remember {
                listOf(
                    "😊" to "I'm feeling happy and energetic today!",
                    "😌" to "I feel calm and at peace right now",
                    "😐" to "I feel okay, just neutral about everything",
                    "😔" to "I feel sad and a bit down",
                    "😠" to "I feel frustrated and irritated"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                moods.forEach { (emoji, text) ->
                    val isSelected = userInput == text

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) {
                                    Color.White.copy(alpha = 0.4f)
                                } else {
                                    Color.Transparent
                                }
                            )
                            .clickable(
                                enabled = !isLoading,
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                onInputChange(text)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = emoji,
                            fontSize = if (isSelected) 36.sp else 32.sp,
                            modifier = Modifier.offset(y = if (isSelected) (-2).dp else 0.dp)
                        )
                    }
                }
            }
        }
    }
}
