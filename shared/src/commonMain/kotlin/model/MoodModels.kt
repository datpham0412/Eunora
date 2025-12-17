package model
import kotlinx.serialization.Serializable


@Serializable
data class MoodEntry(
    val id: String,
    val rawMoodText: String,
    val normalizedMood: NormalizedMood,
    val ai: MoodAIResult,
    val art: MoodArtMetadata,
    val timestamp: Long,
    val highlight: String? = null  // Small moment captured from HIGHLIGHT MARKER
)

@Serializable
data class MoodAIResult(
    val journal: String,
    val advice: String,
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
    val mood: NormalizedMood,
    val assetId: String? = null
)