package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ai.AIService
import com.example.shared.ApiConfig
import org.example.project.ui.MoodInputScreen
import org.example.project.ui.MoodResultScreen
import repository.MoodRepository
import viewmodel.MoodViewModel

@Composable
fun App() {
    // Initialize ViewModel
    val viewModel = remember {
        val aiService = AIService(ApiConfig.GEMINI_API_KEY)
        val repository = MoodRepository(aiService)
        MoodViewModel(repository)
    }

    val uiState by viewModel.state.collectAsState()

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
            when {
                uiState.currentMoodEntry != null -> {
                    // Show results
                    MoodResultScreen(
                        moodEntry = uiState.currentMoodEntry!!,
                        onNewMood = { viewModel.clearMood() }
                    )
                }
                else -> {
                    // Show input screen
                    MoodInputScreen(
                        userInput = uiState.userInput,
                        isLoading = uiState.isLoading,
                        error = uiState.error,
                        onInputChange = { viewModel.onInputChange(it) },
                        onAnalyze = { viewModel.analyzeMood() },
                        onClearError = { viewModel.clearError() }
                    )
                }
            }
        }
    }

    // Cleanup when composable leaves composition
    DisposableEffect(Unit) {
        onDispose {
            viewModel.onCleared()
        }
    }
}
