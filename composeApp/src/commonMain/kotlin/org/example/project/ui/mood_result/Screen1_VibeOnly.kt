package org.example.project.ui.mood_result

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.NormalizedMood
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Screen1_VibeOnly(
    mood: NormalizedMood,
    artAssetId: String?,
    getMoodArtworkResource: (NormalizedMood, String?) -> DrawableResource?
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MoodAbstractBackground(mood)

        val imageResource = getMoodArtworkResource(mood, artAssetId)
        if (imageResource != null) {
            Image(
                painter = painterResource(imageResource),
                contentDescription = "Mood atmosphere",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        MoodNameText(
            mood = mood,
            modifier = Modifier.align(Alignment.Center)
        )
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
            Text(
                text = word,
                fontSize = 58.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.95f),
                textAlign = TextAlign.Center,
                letterSpacing = 6.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
