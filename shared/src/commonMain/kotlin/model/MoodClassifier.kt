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
        // 1. Extreme Stress (Stressed)
        // Absolute Range: Stress > 90%
        s >= 0.8f -> NormalizedMood.STRESSED

        // 2. Excited (Very High Positive, High Energy, Low Stress)
        // Range: P > 75%, E > 75%, S < 50%
        p >= 0.75f && e >= 0.75f && s < 0.5f -> NormalizedMood.EXCITED

        // 3. Depressed (Very Low Positive, Low Energy)
        // Range: P < 35%, E < 35%, S < 60% (To avoid capturing Overwhelmed)
        p <= 0.35f && e <= 0.35f && s < 0.6f -> NormalizedMood.DEPRESSED

        // 4. Angry (High Stress, High Energy, Negative)
        // Range: S > 65%, E > 60%, P < 50%
        s >= 0.65f && e >= 0.6f && p <= 0.5f -> NormalizedMood.ANGRY

        // 5. Overwhelmed (High Stress, Low Positivity) - Catching cases without high energy
        // Range: S > 75%, P < 45%
        s >= 0.75f && p <= 0.45f -> NormalizedMood.OVERWHELMED

        // 6. Anxious (Moderate-High Stress, Negative)
        // Range: S > 60%, P < 50%
        s >= 0.6f && p <= 0.5f -> NormalizedMood.ANXIOUS

        // 7. Happy / Energetic (High Positive, High Energy) - Below Excited Threshold
        // Range: P > 60%, E > 60%
        p >= 0.6f && e >= 0.6f -> NormalizedMood.HAPPY_ENERGETIC

        // 8. Calm (Positive, Low Stress)
        // Range: P > 55%, S < 45%
        p >= 0.55f && s <= 0.45f -> NormalizedMood.CALM_POSITIVE

        // 9. Sad (Low Positive, Low-Mod Energy) - Above Depressed Threshold
        // Range: P < 45%, E < 55%
        p <= 0.45f && e <= 0.55f -> NormalizedMood.SAD

        // 10. Neutral (Everything else)
        else -> NormalizedMood.NEUTRAL
    }
}
