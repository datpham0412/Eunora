package com.example.shared

class TestRunner(
    private val provider: ApiKeyProvider
) {
    fun run() {
        println("KEY FROM PROVIDER = ${provider.geminiKey}")
    }
}
