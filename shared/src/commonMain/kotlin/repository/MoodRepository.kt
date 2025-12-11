package repository

import ai.AIService
import model.MoodEntry
import model.MoodArtMetadata
import kotlin.random.Random
import kotlinx.datetime.Clock

class MoodRepository(private val aiService: AIService) {
    suspend fun analyzeMood(rawMoodText: String): MoodEntry {
        // Call AI Service to interpret mood
        val aiResult = aiService.interpretMood(rawMoodText)
        val normalizedMood = extractNormalizedMood(rawMoodText)

        return MoodEntry(
            id = generateId(),
            rawMoodText = rawMoodText,
            normalizedMood = normalizedMood,
            ai = aiResult,
            art = MoodArtMetadata(),
            timestamp = Clock.System.now().toEpochMilliseconds()
        )
    }
    private fun extractNormalizedMood(text: String): String {
        val lower = text.lowercase()
        return when {
            "stress" in lower || "anxious" in lower -> "stressed"
            "happy" in lower || "joy" in lower -> "happy"
            "sad" in lower || "depress" in lower -> "sad"
            "lonely" in lower || "alone" in lower -> "lonely"
            "angry" in lower || "mad" in lower -> "angry"
            "excited" in lower -> "excited"
            "calm" in lower || "peace" in lower -> "calm"
            else -> "neutral"
        }
    }
    private fun generateId(): String {
        val timestamp = Clock.System.now().toEpochMilliseconds()
        val random = Random.nextInt(10000)
        return "mood_${timestamp}_${random}"
    }
}
