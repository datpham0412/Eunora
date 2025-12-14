package org.example.project.ui.mood_result

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.NormalizedMood
@Composable
fun Screen4_YourReflection(
    mood: NormalizedMood,
    journalText: String
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MoodAbstractBackground(mood)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your Reflection",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.95f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TypewriterText(
                    text = journalText,
                    fontSize = 18.sp,
                    color = Color.White.copy(alpha = 0.90f),
                    textAlign = TextAlign.Center,
                    delayMs = 20L
                )
            }
        }
    }
}
