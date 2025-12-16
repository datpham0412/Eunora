package viewmodel

import ai.AIService
import com.example.shared.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import repository.MoodRepository
object TestViewModel {

    fun run() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                println("🧪 Testing ViewModel Architecture...")
                println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                // TODO: Update this test to use database
                // val aiService = AIService(ApiConfig.GEMINI_API_KEY)
                // val repository = MoodRepository(aiService, database)
                // val viewModel = MoodViewModel(repository)
                println("Test skipped - needs database instance")
                // launch {
                //     viewModel.state.collect { state ->
                //         when {
                //             state.isLoading -> {
                //                 println("⏳ Loading...")
                //             }
                //             state.error != null -> {
                //                 println("❌ Error: ${state.error}")
                //             }
                //             state.currentMoodEntry != null -> {
                //                 val entry = state.currentMoodEntry
                //                 println("\n✅ Mood Analysis Complete!")
                //                 println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                //                 println("📝 Raw Input: ${entry.rawMoodText}")
                //                 println("🏷️  Normalized Mood: ${entry.normalizedMood}")
                //                 println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                //                 println("📔 Journal Entry:")
                //                 println("   ${entry.ai.journal}")
                //                 println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                //                 println("💡 AI Advice:")
                //                 println("   ${entry.ai.advice}")
                //                 println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                //                 println("😊 Emotion Scores:")
                //                 println("   • Positivity: ${entry.ai.emotion.positivity}")
                //                 println("   • Energy: ${entry.ai.emotion.energy}")
                //                 println("   • Stress: ${entry.ai.emotion.stress}")
                //                 println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                //                 println("🎨 Art Prompt:")
                //                 println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                //                 println("🆔 Entry ID: ${entry.id}")
                //                 println("⏰ Timestamp: ${entry.timestamp}")
                //                 println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                //                 println("✅ ViewModel Test Completed!")
                //             }
                //         }
                //     }
                // }
                // println("📝 Setting user input...")
                // viewModel.onInputChange("I feel stressed but hopeful about my future today")
                // println("🚀 Triggering mood analysis...")
                // viewModel.analyzeMood()

            } catch (e: Exception) {
                println("❌ ViewModel Test Failed:")
                println("Error: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
