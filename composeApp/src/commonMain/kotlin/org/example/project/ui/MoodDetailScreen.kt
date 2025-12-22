package org.example.project.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.MoodEntry
import org.example.project.ui.mood_result.*
import util.formatDate
import viewmodel.MoodDetailViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ai_mood_journal.composeapp.generated.resources.Res
import ai_mood_journal.composeapp.generated.resources.*
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import model.NormalizedMood

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodDetailScreen(
    viewModel: MoodDetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.state.collectAsState()
    BackHandler(onBack = onBackClick)

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF9470F4)
                    )
                }
            }

            uiState.entry != null -> {
                MoodDetailContent(
                    entry = uiState.entry!!,
                    onBackClick = onBackClick,
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "❌",
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = uiState.error ?: "Entry not found",
                            color = Color(0xFF742A2A),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        EdgeSwipeBackHandler(onBack = onBackClick)

        if (uiState.entry == null) {
            AdaptiveBackButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(start = 8.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onCleared()
        }
    }
}



@OptIn(ExperimentalResourceApi::class)
@Composable
private fun MoodDetailContent(
    entry: MoodEntry,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val date = formatDate(entry.timestamp)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(extractMoodBackgroundColor(entry.normalizedMood))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(16.dp))
            AdaptiveBackButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .statusBarsPadding()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val imageRes = when (entry.normalizedMood) {
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
                        MoodAbstractBackground(entry.normalizedMood)
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(getMoodEmoji(entry.normalizedMood), fontSize = 64.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
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
                CalmSurface(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Your Takeaway",
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = if (entry.ai.journal.isNotBlank()) entry.ai.journal else "No details added.",
                            fontSize = 16.sp,
                            color = Color(0xFF374151),
                            lineHeight = 24.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                entry.highlight?.let { highlight ->
                    val baseMoodColor = getMoodColor(entry.normalizedMood)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(baseMoodColor.copy(alpha = 0.1f))
                            .padding(20.dp)
                    ) {
                        Column {
                            Text(
                                text = "✨ A Moment to Keep",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = baseMoodColor,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = highlight,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1F2937),
                                lineHeight = 24.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFFF7ED))
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
                            text = entry.ai.advice,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF92400E),
                            lineHeight = 24.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                        text = formatMoodName(entry.normalizedMood),
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                EmotionalSpectrum(
                    label = "Emotional tone",
                    leftLabel = "Difficult",
                    rightLabel = "Positive",
                    position = entry.ai.emotion.positivity,
                    gradient = Brush.horizontalGradient(
                        listOf(
                            Color(0xFF9CA3AF).copy(alpha = 0.3f),
                            Color(0xFF10B981).copy(alpha = 0.3f)
                        )
                    ),
                    markerColor = Color(0xFF10B981),
                    isVisible = true
                )
                Spacer(modifier = Modifier.height(24.dp))

                EmotionalSpectrum(
                    label = "Energy level",
                    leftLabel = "Resting",
                    rightLabel = "Energized",
                    position = entry.ai.emotion.energy,
                    gradient = Brush.horizontalGradient(
                        listOf(
                            Color(0xFFA78BFA).copy(alpha = 0.3f),
                            Color(0xFFF59E0B).copy(alpha = 0.3f)
                        )
                    ),
                    markerColor = Color(0xFFF59E0B),
                    isVisible = true
                )
                Spacer(modifier = Modifier.height(24.dp))

                EmotionalSpectrum(
                    label = "Inner state",
                    leftLabel = "At ease",
                    rightLabel = "Tense",
                    position = entry.ai.emotion.stress,
                    gradient = Brush.horizontalGradient(
                        listOf(
                            Color(0xFF10B981).copy(alpha = 0.3f),
                            Color(0xFFF97316).copy(alpha = 0.3f)
                        )
                    ),
                    markerColor = if (entry.ai.emotion.stress > 0.6f) Color(0xFFF97316) else Color(
                        0xFF10B981
                    ),
                    isVisible = true
                )

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}


