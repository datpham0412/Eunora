package model

/**
 * Normalized mood states derived deterministically from emotion scores.
 *
 * These moods are classified based on three dimensions:
 * - Positivity: 0.0 (very negative) to 1.0 (very positive)
 * - Energy: 0.0 (low energy/tired) to 1.0 (high energy/active)
 * - Stress: 0.0 (relaxed) to 1.0 (highly stressed)
 */
enum class NormalizedMood {
    CALM_POSITIVE,      // High positivity, low stress, moderate energy
    HAPPY_ENERGETIC,    // High positivity, high energy, low stress
    EXCITED,            // Very high positivity and energy
    NEUTRAL,            // Balanced scores, no extreme values
    STRESSED,           // High stress with moderate positivity
    ANXIOUS,            // High stress, low positivity, restless energy
    SAD,                // Low positivity, low energy
    DEPRESSED,          // Very low positivity and energy
    ANGRY,              // Low positivity, high energy, high stress
    OVERWHELMED         // Very high stress, low positivity
}

/**
 * Classifies mood based purely on emotion scores.
 *
 * Decision tree logic (evaluated in order):
 *
 * 1. OVERWHELMED: stress > 0.75 && positivity < 0.5
 *    - Extremely stressed with negative outlook
 *
 * 2. DEPRESSED: positivity < 0.3 && energy < 0.3
 *    - Very low positivity and energy (hopelessness + fatigue)
 *
 * 3. ANGRY: stress > 0.6 && energy > 0.6 && positivity < 0.4
 *    - High stress and energy with negative emotions
 *
 * 4. ANXIOUS: stress > 0.6 && positivity < 0.5
 *    - High stress with negative/neutral emotions
 *
 * 5. EXCITED: positivity > 0.75 && energy > 0.75
 *    - Very high positivity and energy
 *
 * 6. HAPPY_ENERGETIC: positivity > 0.6 && energy > 0.6 && stress < 0.5
 *    - High positivity and energy with low stress
 *
 * 7. CALM_POSITIVE: positivity > 0.6 && stress < 0.4
 *    - High positivity with low stress (peaceful happiness)
 *
 * 8. STRESSED: stress > 0.6
 *    - High stress (catches remaining stressed states)
 *
 * 9. SAD: positivity < 0.4 && energy < 0.5
 *    - Low positivity and low energy
 *
 * 10. NEUTRAL: default
 *     - Balanced emotional state
 *
 * @param emotion The emotion scores from AI analysis
 * @return The classified normalized mood
 */
fun classifyMood(emotion: MoodEmotionScore): NormalizedMood {
    val p = emotion.positivity
    val e = emotion.energy
    val s = emotion.stress

    return when {
        // Critical negative states first
        s > 0.75f && p < 0.5f -> NormalizedMood.OVERWHELMED
        p < 0.3f && e < 0.3f -> NormalizedMood.DEPRESSED

        // High-intensity negative states
        s > 0.6f && e > 0.6f && p < 0.4f -> NormalizedMood.ANGRY
        s > 0.6f && p < 0.5f -> NormalizedMood.ANXIOUS

        // High-intensity positive states
        p > 0.75f && e > 0.75f -> NormalizedMood.EXCITED
        p > 0.6f && e > 0.6f && s < 0.5f -> NormalizedMood.HAPPY_ENERGETIC
        p > 0.6f && s < 0.4f -> NormalizedMood.CALM_POSITIVE

        // Moderate negative states
        s > 0.6f -> NormalizedMood.STRESSED
        p < 0.4f && e < 0.5f -> NormalizedMood.SAD

        // Default balanced state
        else -> NormalizedMood.NEUTRAL
    }
}
