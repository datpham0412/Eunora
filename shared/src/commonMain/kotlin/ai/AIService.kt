package ai

import kotlinx.serialization.json.*
import model.*

class AIService(private val apiKey: String) {

    private val client = GeminiClient(apiKey)

    suspend fun interpretMood(rawText: String): MoodAIResult {

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

        val artPrompt = PromptBuilder.buildArtPrompt(
            normalizedMood = extractNormalizedMood(rawText),
            emotion = Triple(emotion.positivity, emotion.energy, emotion.stress)
        )

        return MoodAIResult(
            journal = journal,
            advice = advice,
            emotion = emotion,
            artPrompt = artPrompt
        )
    }

    private fun extractNormalizedMood(text: String): String {
        val lower = text.lowercase()
        return when {
            "stress" in lower -> "stressed"
            "happy" in lower  -> "happy"
            "sad" in lower    -> "sad"
            "lonely" in lower -> "lonely"
            else -> "neutral"
        }
    }
}

