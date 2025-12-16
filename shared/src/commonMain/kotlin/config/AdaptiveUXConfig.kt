package config

import model.NormalizedMood

/**
 * Mood groups for adaptive UX behavior
 */
enum class MoodGroup {
    HIGH_ACTIVATION,  // ANXIOUS, ANGRY, STRESSED
    LOW_ENERGY,       // DEPRESSED, SAD, OVERWHELMED
    POSITIVE,         // CALM_POSITIVE, HAPPY_ENERGETIC, EXCITED
    NEUTRAL           // NEUTRAL
}

/**
 * Maps normalized moods to mood groups
 */
fun NormalizedMood.toMoodGroup(): MoodGroup {
    return when (this) {
        // High activation, tense states
        NormalizedMood.ANXIOUS,
        NormalizedMood.ANGRY,
        NormalizedMood.STRESSED
        -> MoodGroup.HIGH_ACTIVATION

        // Low energy, negative states
        NormalizedMood.DEPRESSED,
        NormalizedMood.SAD,
        NormalizedMood.OVERWHELMED
        -> MoodGroup.LOW_ENERGY

        // Positive states
        NormalizedMood.CALM_POSITIVE,
        NormalizedMood.HAPPY_ENERGETIC,
        NormalizedMood.EXCITED
        -> MoodGroup.POSITIVE

        // Balanced state
        NormalizedMood.NEUTRAL
        -> MoodGroup.NEUTRAL
    }
}
