package ai
object PromptBuilder {

    fun buildInterpretationPrompt(rawText: String): String =
        """
        You are an emotional analysis assistant.

        Task:
        - Read the user's mood description.
        - Produce a JSON EXACTLY in this format:
        {
            "journal": "...",
            "advice": "...",
            "emotion": {
                "positivity": 0.0 to 1.0,
                "energy": 0.0 to 1.0,
                "stress": 0.0 to 1.0
            }
        }

        User mood description:
        "$rawText"
        """.trimIndent()

    fun buildArtPrompt(normalizedMood: String, emotion: Triple<Float, Float, Float>): String {
        val (positivity, energy, stress) = emotion

        val moodTone = when {
            positivity > 0.7 -> "bright and uplifting"
            stress > 0.7 -> "tense and atmospheric"
            energy > 0.7 -> "energetic and vibrant"
            else -> "soft and introspective"
        }

        return """
            Create an abstract emotional painting that represents the mood: "$normalizedMood".

            Guidelines:
            - Overall tone: $moodTone
            - Use emotional gradients based on values:
              positivity: $positivity
              energy: $energy
              stress: $stress
            - Style: soft, atmospheric, modern, minimalistic.
            - Avoid literal imagery.
        """.trimIndent()
    }
}

