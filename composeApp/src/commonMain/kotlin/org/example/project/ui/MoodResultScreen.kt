package org.example.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.MoodEntry
import model.NormalizedMood
import org.example.project.ui.mood_result.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.DrawableResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MoodResultScreen(
    moodEntry: MoodEntry,
    onNewMood: () -> Unit
) {
    val backgroundColor = extractMoodBackgroundColor(moodEntry.normalizedMood)

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 5 }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> Screen1_VibeOnly(
                    mood = moodEntry.normalizedMood,
                    artAssetId = moodEntry.art.assetId,
                    getMoodArtworkResource = ::getMoodArtworkResource
                )
                1 -> Screen2_MoodMeaning(
                    mood = moodEntry.normalizedMood,
                    emotion = moodEntry.ai.emotion
                )
                2 -> Screen3_EmotionalSpectrums(
                    mood = moodEntry.normalizedMood,
                    emotion = moodEntry.ai.emotion
                )
                3 -> Screen4_YourReflection(
                    mood = moodEntry.normalizedMood,
                    journalText = moodEntry.ai.journal
                )
                4 -> Screen5_GentleGuidance(
                    mood = moodEntry.normalizedMood,
                    adviceText = moodEntry.ai.advice,
                    onNewMood = onNewMood
                )
            }
        }

        PageIndicators(
            pageCount = 5,
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}

@Composable
private fun PageIndicators(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == currentPage) 8.dp else 6.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == currentPage) {
                            Color.White.copy(alpha = 0.9f)
                        } else {
                            Color.White.copy(alpha = 0.4f)
                        }
                    )
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
fun getMoodArtworkResource(mood: NormalizedMood, assetId: String?): DrawableResource? =
    when (mood) {
        else -> null // Fallback to abstract background for moods without images
    }
