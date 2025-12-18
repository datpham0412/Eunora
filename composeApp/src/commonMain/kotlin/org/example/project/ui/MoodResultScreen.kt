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
import ai_mood_journal.composeapp.generated.resources.*

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
    onHistoryClick: () -> Unit = {},
    onHighlightCapture: (String, String?) -> Unit = { _, _ -> },
    onBack: () -> Unit // Callback for system back button
) {
    // Handle System Back Button
    BackHandler(onBack = onBack)

    val backgroundColor = extractMoodBackgroundColor(moodEntry.normalizedMood)
    val moodGroup = moodEntry.normalizedMood.toMoodGroup()

    // Build page flow based on mood group
    val pageFlow = remember(moodGroup) { buildPageFlow(moodGroup) }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pageFlow.size }
    )

    val coroutineScope = rememberCoroutineScope()

    // Local state to track updates (like highlight) during the flow
    var currentEntry by remember(moodEntry) { mutableStateOf(moodEntry) }

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
                            mood = currentEntry.normalizedMood,
                            artAssetId = currentEntry.art.assetId,
                            getMoodArtworkResource = ::getMoodArtworkResource
                        )
                        1 -> Screen2_MoodMeaning(
                            mood = currentEntry.normalizedMood,
                            emotion = currentEntry.ai.emotion,
                            isVisible = pagerState.currentPage == page
                        )
                        2 -> Screen3_EmotionalSpectrums(
                            mood = currentEntry.normalizedMood,
                            emotion = currentEntry.ai.emotion,
                            isVisible = pagerState.currentPage == page
                        )
                        3 -> Screen4_YourReflection(
                            mood = currentEntry.normalizedMood,
                            journalText = currentEntry.ai.journal,
                            isVisible = pagerState.currentPage == page
                        )
                        4 -> Screen5_GentleGuidance(
                            mood = currentEntry.normalizedMood,
                            adviceText = currentEntry.ai.advice,
                            onNewMood = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(page + 1)
                                }
                            },
                            isVisible = pagerState.currentPage == page
                        )
                        5 -> Screen6_Summary(
                            moodEntry = currentEntry,
                            onComplete = onNewMood, // Final exit
                            isVisible = pagerState.currentPage == page
                        )
                    }
                }
                PageType.PauseMarker -> {
                    PauseMarker(
                        mood = currentEntry.normalizedMood,
                        onComplete = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page + 1)
                            }
                        }
                    )
                }
                PageType.PermissionMarker -> {
                    PermissionMarker(
                        mood = currentEntry.normalizedMood,
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
                        mood = currentEntry.normalizedMood,
                        onComplete = { input ->
                            // Save highlight to database
                            onHighlightCapture(currentEntry.id, input)
                            // Update local state so next screens see it
                            currentEntry = currentEntry.copy(highlight = input)
                            
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
                .padding(bottom = 64.dp)
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
            PageType.CoreScreen(4),  // Screen 5: Gentle Guidance
            PageType.CoreScreen(5)   // Screen 6: Summary
        )

        MoodGroup.LOW_ENERGY -> listOf(
            PageType.CoreScreen(0),     // Screen 1: Vibe
            PageType.CoreScreen(1),     // Screen 2: Mood Meaning
            PageType.CoreScreen(2),     // Screen 3: Emotional Spectrums
            PageType.PermissionMarker,  // PERMISSION MARKER
            PageType.CoreScreen(3),     // Screen 4: Your Reflection
            PageType.CoreScreen(4),     // Screen 5: Gentle Guidance
            PageType.CoreScreen(5)      // Screen 6: Summary
        )

        MoodGroup.POSITIVE -> listOf(
            PageType.CoreScreen(0),  // Screen 1: Vibe
            PageType.CoreScreen(1),  // Screen 2: Mood Meaning
            PageType.CoreScreen(2),  // Screen 3: Emotional Spectrums
            PageType.CoreScreen(3),  // Screen 4: Your Reflection
            PageType.HighlightMarker,// HIGHLIGHT MARKER
            PageType.CoreScreen(4),  // Screen 5: Gentle Guidance
            PageType.CoreScreen(5)   // Screen 6: Summary
        )

        MoodGroup.NEUTRAL -> listOf(
            PageType.CoreScreen(0),  // Screen 1: Vibe
            PageType.CoreScreen(1),  // Screen 2: Mood Meaning
            PageType.CoreScreen(2),  // Screen 3: Emotional Spectrums
            PageType.CoreScreen(3),  // Screen 4: Your Reflection
            PageType.CoreScreen(4),  // Screen 5: Gentle Guidance
            PageType.CoreScreen(5)   // Screen 6: Summary
        )
    }
}

/**
 * Check if page is a marker (disable swipe for markers)
 * Note: HighlightMarker and PermissionMarker allow swipe
 */
private fun isMarkerPage(pageType: PageType): Boolean {
    return when (pageType) {
        is PageType.CoreScreen -> false
        PageType.PauseMarker -> true
        PageType.PermissionMarker -> false  // Allow swipe for PERMISSION MARKER
        PageType.HighlightMarker -> false  // Allow swipe for HIGHLIGHT MARKER
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
