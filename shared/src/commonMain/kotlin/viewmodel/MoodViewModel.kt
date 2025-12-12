package viewmodel

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import repository.MoodRepository
class MoodViewModel(
    private val repository: MoodRepository
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("ViewModel Exception: ${exception.message}")
        exception.printStackTrace()
        _state.update {
            it.copy(
                isLoading = false,
                error = "Failed to analyze mood: ${exception.message}"
            )
        }
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main + exceptionHandler)

    private val _state = MutableStateFlow(MoodUIState())
    val state: StateFlow<MoodUIState> = _state.asStateFlow()
    fun onInputChange(input: String) {
        _state.update { it.copy(userInput = input) }
    }
    fun analyzeMood() {
        val input = _state.value.userInput.trim()

        if (input.isEmpty()) {
            _state.update { it.copy(error = "Please enter your mood") }
            return
        }

        scope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                val moodEntry = repository.analyzeMood(input)

                _state.update {
                    it.copy(
                        isLoading = false,
                        currentMoodEntry = moodEntry,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to analyze mood: ${e.message}"
                    )
                }
            }
        }
    }
    fun clearMood() {
        _state.update {
            MoodUIState()
        }
    }
    fun clearError() {
        _state.update { it.copy(error = null) }
    }
    fun onCleared() {
        scope.cancel()
    }
}
