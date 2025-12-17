package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import ai.AIService
import com.example.shared.ApiConfig
import com.example.shared.db.MoodDatabase
import database.DatabaseDriverFactory
import org.example.project.ui.*
import repository.MoodRepository
import viewmodel.*

sealed class Screen {
    object Input : Screen()
    data class Result(val entryId: String) : Screen()
    object History : Screen()
    data class Detail(val entryId: String) : Screen()
}

@Composable
expect fun getPlatformContext(): Any

expect fun createMoodDatabase(context: Any): MoodDatabase

@Composable
fun App() {
    val context = getPlatformContext()

    // Initialize database and repository
    val database = remember(context) { createMoodDatabase(context) }

    val repository = remember {
        val aiService = AIService(ApiConfig.GEMINI_API_KEY)
        MoodRepository(aiService, database)
    }

    // Initialize main ViewModel
    val moodViewModel = remember {
        MoodViewModel(repository)
    }

    val uiState by moodViewModel.state.collectAsState()

    // Navigation state
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Input) }
    var historyViewModel: MoodHistoryViewModel? by remember { mutableStateOf(null) }
    var detailViewModel: MoodDetailViewModel? by remember { mutableStateOf(null) }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = androidx.compose.ui.graphics.Color(0xFF6200EE),
            secondary = androidx.compose.ui.graphics.Color(0xFF03DAC5),
            tertiary = androidx.compose.ui.graphics.Color(0xFFFF6F00)
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (val screen = currentScreen) {
                is Screen.Input -> {
                    MoodInputScreen(
                        userInput = uiState.userInput,
                        isLoading = uiState.isLoading,
                        error = uiState.error,
                        onInputChange = { moodViewModel.onInputChange(it) },
                        onAnalyze = { moodViewModel.analyzeMood() },
                        onClearError = { moodViewModel.clearError() },
                        onHistoryClick = {
                            historyViewModel = MoodHistoryViewModel(repository)
                            currentScreen = Screen.History
                        }
                    )
                }

                is Screen.Result -> {
                    uiState.currentMoodEntry?.let { entry ->
                        val coroutineScope = rememberCoroutineScope()
                        MoodResultScreen(
                            moodEntry = entry,
                            onNewMood = {
                                moodViewModel.clearMood()
                                currentScreen = Screen.Input
                            },
                            onHistoryClick = {
                                historyViewModel = MoodHistoryViewModel(repository)
                                currentScreen = Screen.History
                            },
                            onHighlightCapture = { entryId, highlight ->
                                coroutineScope.launch {
                                    repository.updateHighlight(entryId, highlight)
                                }
                            }
                        )
                    }
                }

                is Screen.History -> {
                    historyViewModel?.let { vm ->
                        MoodHistoryScreen(
                            viewModel = vm,
                            onEntryClick = { entryId ->
                                detailViewModel = MoodDetailViewModel(repository, entryId)
                                currentScreen = Screen.Detail(entryId)
                            },
                            onBackClick = {
                                historyViewModel = null
                                currentScreen = Screen.Input
                            }
                        )
                    }
                }

                is Screen.Detail -> {
                    detailViewModel?.let { vm ->
                        MoodDetailScreen(
                            viewModel = vm,
                            onBackClick = {
                                detailViewModel = null
                                currentScreen = Screen.History
                            }
                        )
                    }
                }
            }
        }
    }

    // Update screen when mood entry is created
    LaunchedEffect(uiState.currentMoodEntry) {
        if (uiState.currentMoodEntry != null) {
            currentScreen = Screen.Result(uiState.currentMoodEntry!!.id)
        }
    }

    // Cleanup when composable leaves composition
    DisposableEffect(Unit) {
        onDispose {
            moodViewModel.onCleared()
            historyViewModel?.onCleared()
            detailViewModel?.onCleared()
        }
    }
}
