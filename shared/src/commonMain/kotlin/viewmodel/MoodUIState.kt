package viewmodel

import model.MoodEntry
data class MoodUIState(
    val isLoading: Boolean = false,
    val currentMoodEntry: MoodEntry? = null,
    val error: String? = null,
    val userInput: String = "",
    val technicalPrompt: String? = null
)
