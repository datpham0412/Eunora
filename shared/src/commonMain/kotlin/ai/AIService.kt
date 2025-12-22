package ai

import kotlinx.serialization.json.*
import model.*

class AIService(private val apiKey: String) {

    private val client = GeminiClient(apiKey)

    suspend fun interpretMood(rawText: String): MoodAIResult {
        return try {
            interpretMoodWithAPI(rawText)
        } catch (e: Exception) {
            println("⚠️ API call failed: ${e.message}")
            println("🔄 Using fallback offline mode")

            generateFallbackMood(rawText, e)
        }
    }

    private suspend fun interpretMoodWithAPI(rawText: String): MoodAIResult {
        val prompt = PromptBuilder.buildInterpretationPrompt(rawText)
        val json = client.generateJsonResponse(prompt)

        println("🔍 Full API Response: ${json.toString()}")

        val textResponse = json["candidates"]
            ?.jsonArray?.firstOrNull()
            ?.jsonObject?.get("content")
            ?.jsonObject?.get("parts")
            ?.jsonArray?.firstOrNull()
            ?.jsonObject?.get("text")
            ?.jsonPrimitive?.content

        if (textResponse == null || textResponse.isEmpty()) {
            println("❌ Empty or null response from API")
            println("❌ Full JSON: ${json.toString()}")
            throw IllegalStateException("Empty response from Gemini API. Check your API key and quota.")
        }

        println("📄 Raw text response: $textResponse")

        // Strip markdown code fences if present
        val cleanedJson = textResponse
            .trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()

        println("🧹 Cleaned JSON: $cleanedJson")

        if (cleanedJson.isEmpty()) {
            throw IllegalStateException("Empty JSON after cleaning")
        }

        val parsed = Json.parseToJsonElement(cleanedJson).jsonObject

        val journal = parsed["journal"]!!.jsonPrimitive.content
        val advice = parsed["advice"]!!.jsonPrimitive.content

        val emotionObj = parsed["emotion"]!!.jsonObject

        val emotion = MoodEmotionScore(
            positivity = emotionObj["positivity"]!!.jsonPrimitive.float,
            energy     = emotionObj["energy"]!!.jsonPrimitive.float,
            stress     = emotionObj["stress"]!!.jsonPrimitive.float,
        )

        return MoodAIResult(
            journal = journal,
            advice = advice,
            emotion = emotion
        )
    }

    /**
     * Fallback mood generation when API is unavailable
     * Provides basic mood classification based on keywords
     */
    private fun generateFallbackMood(rawText: String, exception: Exception): MoodAIResult {
        val lowerText = rawText.lowercase()

        // Simple keyword-based emotion detection
        val (positivity, energy, stress) = when {
            // Positive, high energy
            lowerText.contains(Regex("happy|excited|amazing|great|wonderful|fantastic|energetic|pumped")) ->
                Triple(0.85f, 0.80f, 0.20f)

            // Positive, calm
            lowerText.contains(Regex("calm|peaceful|relaxed|content|good|fine|okay|nice")) ->
                Triple(0.70f, 0.35f, 0.25f)

            // Anxious/stressed
            lowerText.contains(Regex("anxious|worried|stressed|nervous|overwhelmed|panic|tense")) ->
                Triple(0.30f, 0.60f, 0.85f)

            // Angry
            lowerText.contains(Regex("angry|mad|furious|annoyed|frustrated|irritated")) ->
                Triple(0.25f, 0.75f, 0.80f)

            // Sad/depressed
            lowerText.contains(Regex("sad|depressed|down|blue|lonely|empty|hopeless|tired")) ->
                Triple(0.20f, 0.25f, 0.60f)

            // Neutral default
            else -> Triple(0.50f, 0.50f, 0.40f)
        }

        val errorMessage = when {
            exception.message?.contains("Unable to resolve host") == true ||
            exception.message?.contains("No address associated") == true ||
            exception.message?.contains("timeout") == true ->
                "⚠️ Working offline - No internet connection detected"

            exception.message?.contains("API key") == true ||
            exception.message?.contains("401") == true ||
            exception.message?.contains("403") == true ->
                "⚠️ Working offline - API key issue"

            else -> "⚠️ Working offline - AI service unavailable"
        }

        return MoodAIResult(
            journal = "I noticed you're feeling \"$rawText\". $errorMessage, so I can't provide personalized insights right now. But I'm here with you, acknowledging what you're experiencing in this moment.",
            advice = "When you're back online, I'll be able to give you more personalized guidance. For now, take a moment to breathe and be present with your feelings. They're valid, whatever they are.",
            emotion = MoodEmotionScore(
                positivity = positivity,
                energy = energy,
                stress = stress
            )
        )
    }
}

