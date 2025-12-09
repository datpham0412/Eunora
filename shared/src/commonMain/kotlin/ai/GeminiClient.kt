package ai

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

class GeminiClient(private val apiKey: String) {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun generateJsonResponse(prompt: String): JsonObject {

        val response: HttpResponse = client.post(
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"
        ) {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(
                JsonObject(
                    mapOf("contents" to JsonArray(
                        listOf(
                            JsonObject(
                                mapOf("parts" to JsonArray(
                                    listOf(JsonObject(mapOf("text" to JsonPrimitive(prompt))))
                                ))
                            )
                        )
                    ))
                )
            )
        }

        val json = response.body<JsonObject>()
        return json
    }
}

