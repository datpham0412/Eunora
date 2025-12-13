package repository

import ai.AIService
import model.MoodEntry
import model.MoodArtMetadata
import model.classifyMood
import kotlin.random.Random
import util.currentTimeMillis

class MoodRepository(private val aiService: AIService) {
    suspend fun analyzeMood(rawMoodText: String): MoodEntry {
        // Call AI Service to interpret mood
        val aiResult = aiService.interpretMood(rawMoodText)

        // Classify mood deterministically from emotion scores
        val normalizedMood = classifyMood(aiResult.emotion)

        return MoodEntry(
            id = generateId(),
            rawMoodText = rawMoodText,
            normalizedMood = normalizedMood,
            ai = aiResult,
            art = MoodArtMetadata(
                mood = normalizedMood,
                assetId = null  // Will be assigned later from local asset library
            ),
            timestamp = currentTimeMillis()
        )
    }

    private fun generateId(): String {
        val timestamp = currentTimeMillis()
        val random = Random.nextInt(10000)
        return "mood_${timestamp}_${random}"
    }
}
