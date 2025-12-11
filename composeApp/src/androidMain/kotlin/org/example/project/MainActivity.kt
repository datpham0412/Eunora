package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.shared.ApiConfig
import viewmodel.TestViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "=================================")
        Log.d("MainActivity", "GEMINI API KEY: ${ApiConfig.GEMINI_API_KEY}")
        Log.d("MainActivity", "API KEY LENGTH: ${ApiConfig.GEMINI_API_KEY.length}")
        Log.d("MainActivity", "=================================")

        Log.d("MainActivity", "🧪 Starting ViewModel Test...")
        TestViewModel.run()

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