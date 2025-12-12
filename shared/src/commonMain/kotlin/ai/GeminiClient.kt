package ai

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

class GeminiClient(private val apiKey: String) {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private val model = "models/gemini-2.5-flash-lite"
    private val endpoint = "https://generativelanguage.googleapis.com/v1beta/$model:generateContent"

    suspend fun generateJsonResponse(prompt: String): JsonObject {

        println("🌐 Sending request → Gemini 2.5 Flash")
        println("URL: $endpoint")

        val requestBody = buildJsonObject {
            put(
                "contents",
                JsonArray(
                    listOf(
                        buildJsonObject {
                            put(
                                "parts",
                                JsonArray(
                                    listOf(
                                        buildJsonObject {
                                            put("text", JsonPrimitive(prompt))
                                        }
                                    )
                                )
                            )
                        }
                    )
                )
            )
        }

        println("📤 Request body = ${requestBody.toString().take(200)}...")

        val response: HttpResponse = client.post("$endpoint?key=$apiKey") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }

        val responseBody = response.bodyAsText()
        println("📥 Status: ${response.status}")
        println("📥 Body: ${responseBody.take(500)}...")

        if (responseBody.isEmpty()) {
            throw IllegalStateException("❌ Empty response from Gemini API")
        }

        return Json.parseToJsonElement(responseBody).jsonObject
    }
}
