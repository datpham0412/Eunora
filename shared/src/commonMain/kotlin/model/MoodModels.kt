package model
import kotlinx.serialization.Serializable


@Serializable
data class MoodEntry(
    val id: String,
    val rawMoodText: String,
    val normalizedMood: String,
    val ai: MoodAIResult,
    val art: MoodArtMetadata,
    val timestamp: Long
)

@Serializable
data class MoodAIResult(
    val journal: String,
    val advice: String,
    val artPrompt: String,
    val emotion: MoodEmotionScore
)

@Serializable
data class MoodEmotionScore(
    val positivity: Float,
    val energy: Float,
    val stress: Float
)

@Serializable
data class MoodArtMetadata(
    val imageUrl: String? = null,
    val dominantColors: List<String> = emptyList(),
    val seed: Int? = null
)