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
import model.MoodEntry
import repository.MoodRepository

data class MoodDetailUIState(
    val entry: MoodEntry? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class MoodDetailViewModel(
    private val repository: MoodRepository,
    private val entryId: String
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("ViewModel Exception: ${exception.message}")
        exception.printStackTrace()
        _state.update {
            it.copy(
                isLoading = false,
                error = "Failed to load entry: ${exception.message}"
            )
        }
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main + exceptionHandler)

    private val _state = MutableStateFlow(MoodDetailUIState())
    val state: StateFlow<MoodDetailUIState> = _state.asStateFlow()

    init {
        loadEntry()
    }

    private fun loadEntry() {
        scope.launch {
            try {
                val entry = repository.getMoodEntryById(entryId)
                _state.update {
                    it.copy(
                        entry = entry,
                        isLoading = false,
                        error = if (entry == null) "Entry not found" else null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun deleteEntry() {
        scope.launch {
            try {
                repository.deleteMoodEntry(entryId)
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = "Failed to delete entry: ${e.message}")
                }
            }
        }
    }

    fun onCleared() {
        scope.cancel()
    }
}
