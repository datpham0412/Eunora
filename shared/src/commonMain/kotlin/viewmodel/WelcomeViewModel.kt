package viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.NormalizedMood
import repository.MoodRepository

data class WelcomeUIState(
    val totalEntries: Int = 0,
    val averageEnergy: String = "-",
    val mostCommonMoodEmoji: String = "😐",
    val recentMoods: List<WelcomeMoodItem> = emptyList(),
    val isLoading: Boolean = true
)

data class WelcomeMoodItem(
    val id: String,
    val emoji: String,
    val label: String,
    val date: String
)

class WelcomeViewModel(
    private val repository: MoodRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val _state = MutableStateFlow(WelcomeUIState())
    val state: StateFlow<WelcomeUIState> = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        scope.launch {
            repository.getAllMoodEntries()
                .catch { e ->
                    // Handle error silently or simplistic
                    _state.update { it.copy(isLoading = false) }
                }
                .collect { entries ->
                    if (entries.isEmpty()) {
                        _state.update {
                            it.copy(
                                totalEntries = 0,
                                averageEnergy = "-",
                                mostCommonMoodEmoji = "😐",
                                recentMoods = emptyList(),
                                isLoading = false
                            )
                        }
                    } else {
                        val total = entries.size
                        
                        val avgEnergyVal = entries.map { it.ai.emotion.energy }.average()
                        val avgEnergyStr = if (avgEnergyVal.isNaN()) "-" else {
                            val rounded = kotlin.math.round(avgEnergyVal * 10 * 10) / 10.0
                            "$rounded"
                        }

                        val mostCommon = entries.groupingBy { it.normalizedMood }
                            .eachCount()
                            .maxByOrNull { it.value }
                            ?.key
                        val commonEmoji = if (mostCommon != null) getMoodEmoji(mostCommon) else "😐"

                        val recent = entries.sortedByDescending { it.timestamp }
                            .take(3)
                            .map { entry ->
                                WelcomeMoodItem(
                                    id = entry.id,
                                    emoji = getMoodEmoji(entry.normalizedMood),
                                    label = formatMoodLabel(entry.normalizedMood.name),
                                    date = formatDate(entry.timestamp)
                                )
                            }

                        _state.update {
                            it.copy(
                                totalEntries = total,
                                averageEnergy = avgEnergyStr,
                                mostCommonMoodEmoji = commonEmoji,
                                recentMoods = recent,
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun formatMoodLabel(name: String): String {
         return name.split('_')
            .joinToString(" ") { word ->
                word.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }
            .split(" ")
            .firstOrNull() ?: name // Just take the first word for brevity (e.g. "Calm" from "CALM_POSITIVE")
    }

    private fun formatDate(timestamp: Long): String {
        val daysSinceEpoch = (timestamp / (24 * 60 * 60 * 1000L)).toInt()
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

    private fun getMoodEmoji(mood: NormalizedMood): String {
        return when (mood) {
            NormalizedMood.CALM_POSITIVE -> "😌"
            NormalizedMood.HAPPY_ENERGETIC -> "😊"
            NormalizedMood.EXCITED -> "🤩"
            NormalizedMood.NEUTRAL -> "😐"
            NormalizedMood.STRESSED -> "😓"
            NormalizedMood.ANXIOUS -> "😰"
            NormalizedMood.SAD -> "😢"
            NormalizedMood.DEPRESSED -> "😞"
            NormalizedMood.ANGRY -> "😠"
            NormalizedMood.OVERWHELMED -> "😵"
        }
    }
}
