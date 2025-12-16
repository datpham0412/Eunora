package org.example.project.ui.mood_result

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * PAUSE MARKER - HIGH ACTIVATION
 * 3-second forced pause with no interaction
 */
@Composable
fun PauseMarker(
    onComplete: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(3000)
        onComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F0F2)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "You don't need to react right now.",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2C3E50),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 48.dp)
        )
    }
}

/**
 * PERMISSION MARKER - LOW ENERGY
 * Choice to continue or end the flow
 */
@Composable
fun PermissionMarker(
    onContinue: () -> Unit,
    onEnd: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(horizontal = 48.dp)
        ) {
            Text(
                text = "It's okay to stop here.",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF374151),
                textAlign = TextAlign.Center
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // End button
                Button(
                    onClick = onEnd,
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE5E7EB)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        "End",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF374151)
                    )
                }

                // Continue button
                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6B7280)
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

/**
 * HIGHLIGHT MARKER - POSITIVE
 * Optional text input to capture moment
 */
@Composable
fun HighlightMarker(
    onComplete: (String?) -> Unit
) {
    var inputText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBEB)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(horizontal = 48.dp)
        ) {
            Text(
                text = "What's one thing worth remembering?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF92400E),
                textAlign = TextAlign.Center
            )

            // Text input
            BasicTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = Color(0xFF374151),
                    textAlign = TextAlign.Center
                ),
                cursorBrush = SolidColor(Color(0xFF92400E)),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (inputText.isEmpty()) {
                            Text(
                                text = "Type here (optional)",
                                fontSize = 18.sp,
                                color = Color(0xFF9CA3AF),
                                textAlign = TextAlign.Center
                            )
                        }
                        innerTextField()
                    }
                }
            )

            // Continue button
            Button(
                onClick = { onComplete(inputText.ifBlank { null }) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF59E0B)
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
