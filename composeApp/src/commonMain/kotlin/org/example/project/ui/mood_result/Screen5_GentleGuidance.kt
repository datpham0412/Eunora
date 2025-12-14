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

@Composable
fun Screen5_GentleGuidance(
    mood: NormalizedMood,
    adviceText: String,
    onNewMood: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MoodAbstractBackground(mood)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .padding(vertical = 60.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gentle Guidance",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.95f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    TypewriterText(
                        text = adviceText,
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = 0.90f),
                        textAlign = TextAlign.Center,
                        delayMs = 20L
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

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
