package org.example.project.ui.mood_result

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import model.MoodEmotionScore
import model.NormalizedMood

@Composable
fun Screen2_MoodMeaning(
    mood: NormalizedMood,
    emotion: MoodEmotionScore
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MoodAbstractBackground(mood)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = getMoodEmoji(mood),
                fontSize = 80.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Text(
                text = formatMoodName(mood),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.95f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            TypewriterText(
                text = getReflectiveSummary(emotion),
                fontSize = 20.sp,
                color = Color.White.copy(alpha = 0.90f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun TypewriterText(
    text: String,
    fontSize: androidx.compose.ui.unit.TextUnit,
    color: Color,
    textAlign: TextAlign,
    modifier: Modifier = Modifier,
    delayMs: Long = 30L
) {
    var visibleText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        visibleText = ""
        text.forEachIndexed { index, _ ->
            delay(delayMs)
            visibleText = text.substring(0, index + 1)
        }
    }

    Text(
        text = visibleText,
        fontSize = fontSize,
        fontWeight = FontWeight.Normal,
        color = color,
        textAlign = textAlign,
        lineHeight = 32.sp,
        modifier = modifier
    )
}
