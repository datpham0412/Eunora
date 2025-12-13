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
}

