package viewmodel

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.MoodEntry
import repository.MoodRepository
import util.currentTimeMillis

data class MoodHistoryUIState(
    val groupedEntries: Map<String, List<MoodEntry>> = emptyMap(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class MoodHistoryViewModel(
    private val repository: MoodRepository
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("ViewModel Exception: ${exception.message}")
        exception.printStackTrace()
        _state.update {
            it.copy(
                isLoading = false,
                error = "Failed to load history: ${exception.message}"
            )
        }
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main + exceptionHandler)

    private val _state = MutableStateFlow(MoodHistoryUIState())
    val state: StateFlow<MoodHistoryUIState> = _state.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        scope.launch {
            repository.getAllMoodEntries()
                .catch { e ->
                    _state.update {
                        it.copy(
                            error = e.message,
                            isLoading = false
                        )
                    }
                }
                .collect { entries ->
                    val grouped = groupEntriesByDate(entries)
                    _state.update {
                        it.copy(
                            groupedEntries = grouped,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun deleteMoodEntry(entryId: String) {
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

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun groupEntriesByDate(entries: List<MoodEntry>): Map<String, List<MoodEntry>> {
        val now = currentTimeMillis()
        val oneDayMillis = 24 * 60 * 60 * 1000L

        // Calculate start of today (midnight) - rough approximation
        val todayStart = now - (now % oneDayMillis)
        val yesterdayStart = todayStart - oneDayMillis
        val yesterdayEnd = todayStart - 1

        return entries.groupBy { entry ->
            when {
                entry.timestamp >= todayStart -> "Today"
                entry.timestamp in yesterdayStart..yesterdayEnd -> "Yesterday"
                else -> formatDateFromTimestamp(entry.timestamp)
            }
        }
    }

    private fun formatDateFromTimestamp(timestamp: Long): String {
        // Simple date formatting without kotlinx.datetime
        // This is a basic approximation - format as "MMM DD"
        val daysSinceEpoch = (timestamp / (24 * 60 * 60 * 1000L)).toInt()

        // Approximate year, month, day calculation
        val year = 1970 + (daysSinceEpoch / 365)
        val dayOfYear = daysSinceEpoch % 365

        val monthDays = listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun",
                               "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

        var remainingDays = dayOfYear
        var month = 0
        for (i in monthDays.indices) {
            if (remainingDays <= monthDays[i]) {
                month = i
                break
            }
            remainingDays -= monthDays[i]
        }

        val day = remainingDays + 1
        return "${monthNames.getOrNull(month) ?: "Jan"} $day"
    }

    fun onCleared() {
        scope.cancel()
    }
}
