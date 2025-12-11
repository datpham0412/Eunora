package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.shared.ApiConfig
import ai.TestAI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Log API key to verify it's loaded correctly
        Log.d("MainActivity", "=================================")
        Log.d("MainActivity", "GEMINI API KEY: ${ApiConfig.GEMINI_API_KEY}")
        Log.d("MainActivity", "API KEY LENGTH: ${ApiConfig.GEMINI_API_KEY.length}")
        Log.d("MainActivity", "=================================")

        // Test AI Service
        Log.d("MainActivity", "🧪 Starting AI Service Test...")
        TestAI.run()

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}