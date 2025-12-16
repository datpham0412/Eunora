package org.example.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import config.MoodGroup
import config.toMoodGroup
import kotlinx.coroutines.launch
import model.MoodEntry
import model.NormalizedMood
import org.example.project.ui.mood_result.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.DrawableResource

/**
 * Page content types
 */
sealed class PageType {
    data class CoreScreen(val index: Int) : PageType()
    object PauseMarker : PageType()
    object PermissionMarker : PageType()
    object HighlightMarker : PageType()
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MoodResultScreen(
    moodEntry: MoodEntry,
    onNewMood: () -> Unit,
    onHistoryClick: () -> Unit = {}
) {
    val backgroundColor = extractMoodBackgroundColor(moodEntry.normalizedMood)
    val moodGroup = moodEntry.normalizedMood.toMoodGroup()

    // Build page flow based on mood group
    val pageFlow = remember(moodGroup) { buildPageFlow(moodGroup) }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pageFlow.size }
    )

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = !isMarkerPage(pageFlow[pagerState.currentPage])
        ) { page ->
            when (val pageType = pageFlow[page]) {
                is PageType.CoreScreen -> {
                    when (pageType.index) {
                        0 -> Screen1_VibeOnly(
                            mood = moodEntry.normalizedMood,
                            artAssetId = moodEntry.art.assetId,
                            getMoodArtworkResource = ::getMoodArtworkResource
                        )
                        1 -> Screen2_MoodMeaning(
                            mood = moodEntry.normalizedMood,
                            emotion = moodEntry.ai.emotion,
                            isVisible = pagerState.currentPage == page
                        )
                        2 -> Screen3_EmotionalSpectrums(
                            mood = moodEntry.normalizedMood,
                            emotion = moodEntry.ai.emotion,
                            isVisible = pagerState.currentPage == page
                        )
                        3 -> Screen4_YourReflection(
                            mood = moodEntry.normalizedMood,
                            journalText = moodEntry.ai.journal,
                            isVisible = pagerState.currentPage == page
                        )
                        4 -> Screen5_GentleGuidance(
                            mood = moodEntry.normalizedMood,
                            adviceText = moodEntry.ai.advice,
                            onNewMood = onNewMood,
                            isVisible = pagerState.currentPage == page
                        )
                    }
                }
                PageType.PauseMarker -> {
                    PauseMarker(
                        onComplete = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page + 1)
                            }
                        }
                    )
                }
                PageType.PermissionMarker -> {
                    PermissionMarker(
                        onContinue = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page + 1)
                            }
                        },
                        onEnd = onNewMood
                    )
                }
                PageType.HighlightMarker -> {
                    HighlightMarker(
                        onComplete = { input ->
                            // Optional: Save input somewhere if needed
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page + 1)
                            }
                        }
                    )
                }
            }
        }

        PageIndicators(
            pageCount = pageFlow.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}

/**
 * Build page flow based on mood group
 */
private fun buildPageFlow(moodGroup: MoodGroup): List<PageType> {
    return when (moodGroup) {
        MoodGroup.HIGH_ACTIVATION -> listOf(
            PageType.CoreScreen(0),  // Screen 1: Vibe
            PageType.PauseMarker,    // PAUSE MARKER
            PageType.CoreScreen(1),  // Screen 2: Mood Meaning
            PageType.CoreScreen(2),  // Screen 3: Emotional Spectrums
            PageType.CoreScreen(3),  // Screen 4: Your Reflection
            PageType.CoreScreen(4)   // Screen 5: Gentle Guidance
        )

        MoodGroup.LOW_ENERGY -> listOf(
            PageType.CoreScreen(0),     // Screen 1: Vibe
            PageType.CoreScreen(1),     // Screen 2: Mood Meaning
            PageType.CoreScreen(2),     // Screen 3: Emotional Spectrums
            PageType.PermissionMarker,  // PERMISSION MARKER
            PageType.CoreScreen(3),     // Screen 4: Your Reflection
            PageType.CoreScreen(4)      // Screen 5: Gentle Guidance
        )

        MoodGroup.POSITIVE -> listOf(
            PageType.CoreScreen(0),  // Screen 1: Vibe
            PageType.CoreScreen(1),  // Screen 2: Mood Meaning
            PageType.CoreScreen(2),  // Screen 3: Emotional Spectrums
            PageType.CoreScreen(3),  // Screen 4: Your Reflection
            PageType.HighlightMarker,// HIGHLIGHT MARKER
            PageType.CoreScreen(4)   // Screen 5: Gentle Guidance
        )

        MoodGroup.NEUTRAL -> listOf(
            PageType.CoreScreen(0),  // Screen 1: Vibe
            PageType.CoreScreen(1),  // Screen 2: Mood Meaning
            PageType.CoreScreen(2),  // Screen 3: Emotional Spectrums
            PageType.CoreScreen(3),  // Screen 4: Your Reflection
            PageType.CoreScreen(4)   // Screen 5: Gentle Guidance
        )
    }
}

/**
 * Check if page is a marker (disable swipe for markers)
 */
private fun isMarkerPage(pageType: PageType): Boolean {
    return when (pageType) {
        is PageType.CoreScreen -> false
        PageType.PauseMarker,
        PageType.PermissionMarker,
        PageType.HighlightMarker -> true
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
