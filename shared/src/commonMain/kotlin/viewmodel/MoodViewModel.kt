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
                error = getFriendlyErrorMessage(exception)
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
                        error = getFriendlyErrorMessage(e)
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

    private fun getFriendlyErrorMessage(exception: Throwable): String {
        val message = exception.message ?: ""

        return when {
            message.contains("Unable to resolve host") ||
            message.contains("No address associated") ||
            message.contains("UnknownHostException") ||
            message.contains("timeout") ||
            message.contains("SocketTimeoutException") ->
                "No internet connection. Please check your network and try again."

            message.contains("API key") ||
            message.contains("401") ||
            message.contains("403") ->
                "API authentication failed. Please check your API key configuration."

            message.contains("429") ||
            message.contains("quota") ->
                "API quota exceeded. Please try again later."

            message.contains("500") ||
            message.contains("502") ||
            message.contains("503") ->
                "Service temporarily unavailable. Please try again in a moment."

            else ->
                "Something went wrong. Please try again."
        }
    }
}
