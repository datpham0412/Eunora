package org.example.project

import androidx.compose.ui.window.ComposeUIViewController
import com.example.shared.ApiConfig
import viewmodel.TestViewModel

fun MainViewController() = ComposeUIViewController {
    println("=================================")
    println("GEMINI API KEY: ${ApiConfig.GEMINI_API_KEY}")
    println("API KEY LENGTH: ${ApiConfig.GEMINI_API_KEY.length}")
    println("=================================")

    println("🧪 Starting ViewModel Test...")
    TestViewModel.run()

    App()
}