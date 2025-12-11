package ai

import com.example.shared.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object TestAI {

    fun run(apiKey: String = ApiConfig.GEMINI_API_KEY) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                println("🧪 Testing AI Service...")
                println("Using API Key: ${apiKey.take(10)}...")

                val ai = AIService(apiKey)
                val result = ai.interpretMood("I feel stressed but hopeful today...")

                println("✅ AI Service Test Results:")
                println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                println("📝 Journal: ${result.journal}")
                println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                println("💡 Advice: ${result.advice}")
                println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                println("😊 Emotion Scores:")
                println("   • Positivity: ${result.emotion.positivity}")
                println("   • Energy: ${result.emotion.energy}")
                println("   • Stress: ${result.emotion.stress}")
                println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                println("🎨 Art Prompt: ${result.artPrompt}")
                println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                println("✅ Test completed successfully!")
            } catch (e: Exception) {
                println("❌ Test failed with error:")
                println("Error: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}

