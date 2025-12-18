package org.example.project.ui.mood_result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.MoodEntry
import model.NormalizedMood
import ai_mood_journal.composeapp.generated.resources.*

import util.formatDate
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

import androidx.compose.ui.graphics.Brush

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Screen6_Summary(
    moodEntry: MoodEntry,
    onComplete: () -> Unit,
    isVisible: Boolean = true
) {
    if (!isVisible) return

    val scrollState = rememberScrollState()

    // Date formatting using centralized helper
    val date = formatDate(moodEntry.timestamp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(extractMoodBackgroundColor(moodEntry.normalizedMood))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(top = 64.dp, bottom = 100.dp), // Increased top padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Mood Image
            val imageRes = when (moodEntry.normalizedMood) {
                NormalizedMood.CALM_POSITIVE -> Res.drawable.mood_calm_positive
                NormalizedMood.HAPPY_ENERGETIC -> Res.drawable.mood_happy_energetic
                NormalizedMood.EXCITED -> Res.drawable.mood_excited
                NormalizedMood.NEUTRAL -> Res.drawable.mood_neutral
                NormalizedMood.STRESSED -> Res.drawable.mood_stressed
                NormalizedMood.ANXIOUS -> Res.drawable.mood_anxious
                NormalizedMood.SAD -> Res.drawable.mood_sad
                NormalizedMood.DEPRESSED -> Res.drawable.mood_depressed
                NormalizedMood.ANGRY -> Res.drawable.mood_angry
                NormalizedMood.OVERWHELMED -> Res.drawable.mood_overwhelmed
            }

            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.5f))
            ) {
                if (imageRes != null) {
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = "Mood Illustration",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Fallback: Abstract Wave or Emoji
                    MoodAbstractBackground(moodEntry.normalizedMood)
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(getMoodEmoji(moodEntry.normalizedMood), fontSize = 64.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Header Row: "Your Reflection" + Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Reflection",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = date,
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. User Journal (Your Takeaway)
            CalmSurface(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Your Takeaway",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = if (moodEntry.ai.journal.isNotBlank()) moodEntry.ai.journal else "No details added.",
                        fontSize = 16.sp,
                        color = Color(0xFF374151),
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3b. Highlight (Positive Moods)
            moodEntry.highlight?.let { highlight ->
                val baseMoodColor = getMoodColor(moodEntry.normalizedMood)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(baseMoodColor.copy(alpha = 0.1f)) // Dynamic tinted background
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "✨ A Moment to Keep",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = baseMoodColor, // Dynamic title color
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = highlight,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1F2937), // Dark gray for safe readability vs mood colors
                            lineHeight = 24.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 4. Highlight Box -> Full Gentle Advice
            // "yellow part should display full gentle advice"
            // Using the same style (Beige box)
             Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFFF7ED)) // Light Beige/Orange
                    .padding(20.dp)
            ) {
                Column {
                   Text(
                        text = "Daily Advice",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9a3412),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = moodEntry.ai.advice, // FULL ADVICE
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF92400E),
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Emotional Spectrum Heading
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Emotional Spectrum",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = formatMoodName(moodEntry.normalizedMood),
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // 5b. Spectrum Sliders (Using EmotionalSpectrum from Screen 3)
            // Emotional Tone
            EmotionalSpectrum(
                label = "Emotional tone",
                leftLabel = "Difficult",
                rightLabel = "Positive",
                position = moodEntry.ai.emotion.positivity,
                gradient = Brush.horizontalGradient(
                    listOf(
                        Color(0xFF9CA3AF).copy(alpha = 0.3f),
                        Color(0xFF10B981).copy(alpha = 0.3f)
                    )
                ),
                markerColor = Color(0xFF10B981),
                isVisible = isVisible
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            // Energy
            EmotionalSpectrum(
                label = "Energy level",
                leftLabel = "Resting",
                rightLabel = "Energized",
                position = moodEntry.ai.emotion.energy,
                gradient = Brush.horizontalGradient(
                    listOf(
                        Color(0xFFA78BFA).copy(alpha = 0.3f), // Using colors from MoodDetail/Screen3
                        Color(0xFFF59E0B).copy(alpha = 0.3f)
                    )
                ),
                markerColor = Color(0xFFF59E0B),
                isVisible = isVisible
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            // Inner State
            EmotionalSpectrum(
                label = "Inner state",
                leftLabel = "At ease",
                rightLabel = "Tense",
                position = moodEntry.ai.emotion.stress,
                gradient = Brush.horizontalGradient(
                    listOf(
                        Color(0xFF10B981).copy(alpha = 0.3f),
                        Color(0xFFF97316).copy(alpha = 0.3f)
                    )
                ),
                markerColor = if (moodEntry.ai.emotion.stress > 0.6f) Color(0xFFF97316) else Color(0xFF10B981),
                isVisible = isVisible
            )

            Spacer(modifier = Modifier.height(48.dp))
            
            // Done Button (Moved here)
            Button(
                onClick = onComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = getMoodColor(moodEntry.normalizedMood),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    "Done",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Spacer to lift button above pagination dots (dots are usually ~64dp up)
            // But if button is scrolled, it might be behind dots if at very bottom. 
            // The padding(bottom=100.dp) on Column handles the scroll space.
        }
    }
}




