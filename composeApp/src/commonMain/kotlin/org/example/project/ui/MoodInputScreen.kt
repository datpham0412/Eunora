package org.example.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MoodInputScreen(
    userInput: String,
    isLoading: Boolean,
    error: String?,
    onInputChange: (String) -> Unit,
    onAnalyze: () -> Unit,
    onClearError: () -> Unit,
    onHistoryClick: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    // Theme Colors based on Mockup
    val BackgroundColor = Color(0xFFE2F4F2) // Pale Mint Green
    val PrimaryText = Color(0xFF11423D) // Dark Deep Teal
    val SecondaryText = Color(0xFF11423D).copy(alpha = 0.8f)
    val ButtonColor = Color(0xFF3EA8A3) // Teal/Cyan Button
    val CardColor = Color.White
    val MoodItemColor = Color.White

    Scaffold(
        containerColor = BackgroundColor,
        contentColor = PrimaryText
    ) { paddingValues ->
        // Handle System Back Press (Android)
        BackHandler(enabled = true, onBack = onBack)
        
        // Handle Swipe Back (iOS style)
        Box(modifier = Modifier.fillMaxSize()) {
             // Main Content
             Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { focusManager.clearFocus() }
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(32.dp))


            // Title
            Text(
                text = "How are you feeling?",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle
            Text(
                text = "Share your thoughts freely.\nThere's no right or wrong.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 17.sp,
                    lineHeight = 24.sp,
                    color = SecondaryText
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Input Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(24.dp),
                color = CardColor,
                shadowElevation = 0.dp
            ) {
                Box(modifier = Modifier.padding(24.dp)) {
                     BasicTextField(
                        value = userInput,
                        onValueChange = onInputChange,
                        modifier = Modifier.fillMaxSize(),
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF374151), // Dark Gray for input
                        ),
                        enabled = !isLoading,
                        decorationBox = { innerTextField ->
                            if (userInput.isEmpty()) {
                                Text(
                                    text = "Write about your mood,\nthoughts, or feelings...",
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        lineHeight = 28.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFF9CA3AF) // Placeholder Gray
                                    )
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }
            
            if (error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = Color.Red.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Quick Mood Label
            Text(
                text = "Quick mood (optional):",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryText
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Custom Mood Selector Row
            val moods = remember {
                listOf(
                    MoodOption("😊", "Happy", "I'm feeling happy and energetic today!"),
                    MoodOption("😌", "Calm", "I feel calm and at peace right now"),
                    MoodOption("😐", "Neutral", "I feel okay, just neutral about everything"),
                    MoodOption("😓", "Sad", "I feel sad and a bit down"),
                    MoodOption("😠", "Angry", "I feel frustrated and irritated")
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                moods.forEach { mood ->
                    MoodItemButton(
                        emoji = mood.emoji,
                        label = mood.label,
                        isSelected = userInput == mood.textValue,
                        onClick = { onInputChange(mood.textValue) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Continue Button
            Button(
                onClick = onAnalyze,
                enabled = !isLoading && userInput.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = if (!isLoading && userInput.isNotBlank()) 4.dp else 0.dp,
                        shape = RoundedCornerShape(28.dp),
                        spotColor = ButtonColor.copy(alpha = 0.5f)
                    ),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonColor,
                    disabledContainerColor = ButtonColor.copy(alpha = 0.5f),
                    contentColor = Color.White,
                    disabledContentColor = Color.White.copy(alpha = 0.7f)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = "Continue",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
        
        // Swipe Detector Surface (Invisible, left edge)
        EdgeSwipeBackHandler(onBack = onBack)
        }
    }
}

data class MoodOption(val emoji: String, val label: String, val textValue: String)

@Composable
fun MoodItemButton(
    emoji: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(56.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (isSelected) Color(0xFF3EA8A3).copy(alpha = 0.1f) else Color.White,
            border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF3EA8A3)) else null,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp)) // Clip ripple to shape
                .clickable { onClick() }
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = emoji,
                    fontSize = 28.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF11423D),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
