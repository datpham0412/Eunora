package com.example.shared
import platform.Foundation.NSBundle

class IOSApiKeyProvider : ApiKeyProvider {
    override val geminiKey: String
        get() = NSBundle.mainBundle
            .objectForInfoDictionaryKey("GEMINI_API_KEY") as? String ?: ""
}

