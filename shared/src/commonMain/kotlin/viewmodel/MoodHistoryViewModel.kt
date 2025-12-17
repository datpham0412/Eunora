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
import model.NormalizedMood
import repository.MoodRepository
import util.currentTimeMillis

enum class MoodFilter(val displayName: String) {
    ALL("All"),
    HAPPY("Happy"),
    CALM("Calm"),
    SAD("Sad"),
    STRESSED("Stressed") // 4th Group
}

data class MoodHistoryUIState(
    val entries: List<MoodEntry> = emptyList(), // All raw entries
    val groupedFilteredEntries: Map<String, List<MoodEntry>> = emptyMap(),
    val selectedFilter: MoodFilter = MoodFilter.ALL,
    
    // Stats
    val totalEntries: Int = 0,
    val averageMood: String = "-",
    val mostCommonMood: String = "-",
    
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
                    _state.update { currentState ->
                        val stats = calculateStats(entries)
                        val filteredGrouped = filterAndGroupEntries(entries, currentState.selectedFilter)
                        
                        currentState.copy(
                            entries = entries,
                            groupedFilteredEntries = filteredGrouped,
                            totalEntries = stats.total,
                            averageMood = stats.avgStr,
                            mostCommonMood = stats.common,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }
    
    fun setFilter(filter: MoodFilter) {
        _state.update { currentState ->
            val filteredGrouped = filterAndGroupEntries(currentState.entries, filter)
            currentState.copy(
                selectedFilter = filter,
                groupedFilteredEntries = filteredGrouped
            )
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

    private fun filterAndGroupEntries(entries: List<MoodEntry>, filter: MoodFilter): Map<String, List<MoodEntry>> {
        val filtered = if (filter == MoodFilter.ALL) {
            entries
        } else {
            entries.filter { entry ->
                getFilterForMood(entry.normalizedMood) == filter
            }
        }
        return groupEntriesByDate(filtered)
    }
    
    private fun getFilterForMood(mood: NormalizedMood): MoodFilter {
        return when (mood) {
            NormalizedMood.HAPPY_ENERGETIC, NormalizedMood.EXCITED -> MoodFilter.HAPPY
            NormalizedMood.CALM_POSITIVE, NormalizedMood.NEUTRAL -> MoodFilter.CALM
            NormalizedMood.SAD, NormalizedMood.DEPRESSED -> MoodFilter.SAD
            // Stressed, Anxious, Angry, Overwhelmed
            NormalizedMood.STRESSED, NormalizedMood.ANXIOUS, NormalizedMood.ANGRY, NormalizedMood.OVERWHELMED -> MoodFilter.STRESSED
        }
    }

    data class CheckInStats(val total: Int, val avgStr: String, val common: String)

    private fun calculateStats(entries: List<MoodEntry>): CheckInStats {
        if (entries.isEmpty()) return CheckInStats(0, "-", "-")
        
        val total = entries.size
        
        // Avg Energy Calculation (matching WelcomeViewModel)
        val avgEnergyVal = entries.map { it.ai.emotion.energy }.average()
        val avgEnergyStr = if (avgEnergyVal.isNaN()) "-" else {
            val rounded = kotlin.math.round(avgEnergyVal * 10 * 10) / 10.0
            "$rounded"
        }
        
        // Find most common emoji
        // We need to map each entry to an emoji first
        val common = entries
            .groupingBy { getEmojiForMood(it.normalizedMood) }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key ?: "-"
        
        return CheckInStats(total, avgEnergyStr, common) 
    }
    
    private fun getEmojiForMood(mood: NormalizedMood): String {
        return when (mood) {
            NormalizedMood.CALM_POSITIVE -> "😌"
            NormalizedMood.HAPPY_ENERGETIC -> "😊"
            NormalizedMood.EXCITED -> "🤩"
            NormalizedMood.NEUTRAL -> "😐"
            NormalizedMood.STRESSED -> "😫"
            NormalizedMood.ANXIOUS -> "😰"
            NormalizedMood.SAD -> "😓"
            NormalizedMood.DEPRESSED -> "😞"
            NormalizedMood.ANGRY -> "😠"
            NormalizedMood.OVERWHELMED -> "😵"
        }
    }

    private fun groupEntriesByDate(entries: List<MoodEntry>): Map<String, List<MoodEntry>> {
        val now = currentTimeMillis()
        val oneDayMillis = 24 * 60 * 60 * 1000L
        val todayStart = now - (now % oneDayMillis)
        val yesterdayStart = todayStart - oneDayMillis
        
        // Sort by timestamp desc
        val sorted = entries.sortedByDescending { it.timestamp }

        return sorted.groupBy { entry ->
            when {
                entry.timestamp >= todayStart -> "Today"
                entry.timestamp >= yesterdayStart -> "Yesterday"
                else -> formatDateFromTimestamp(entry.timestamp)
            }
        }
    }

    private fun formatDateFromTimestamp(timestamp: Long): String {
        val daysSinceEpoch = (timestamp / (24 * 60 * 60 * 1000L)).toInt()
        val year = 1970 + (daysSinceEpoch / 365)
        val dayOfYear = daysSinceEpoch % 365
        val monthDays = listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
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

