package ai

import kotlinx.coroutines.runBlocking

object TestAI {

    fun run(apiKey: String) = runBlocking {
        val ai = AIService(apiKey)

        val result = ai.interpretMood("I feel stressed but hopeful today...")

        println("journal = ${result.journal}")
        println("advice = ${result.advice}")
        println("emotion = ${result.emotion}")
        println("artPrompt = ${result.artPrompt}")
    }
}

