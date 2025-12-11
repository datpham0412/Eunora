package org.example.project

import androidx.compose.ui.window.ComposeUIViewController
import com.example.shared.ApiConfig

fun MainViewController() = ComposeUIViewController {
    // Log API key to verify it's loaded correctly
    println("=================================")
    println("GEMINI API KEY: ${ApiConfig.GEMINI_API_KEY}")
    println("API KEY LENGTH: ${ApiConfig.GEMINI_API_KEY.length}")
    println("=================================")

    App()
}