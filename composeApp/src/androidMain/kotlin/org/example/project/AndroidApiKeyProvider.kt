package org.example.project
import com.example.shared.ApiKeyProvider

class AndroidApiKeyProvider : ApiKeyProvider {
    override val geminiKey: String
        get() = BuildConfig.GEMINI_API_KEY
}