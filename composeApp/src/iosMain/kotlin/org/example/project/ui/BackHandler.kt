package org.example.project.ui

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // iOS standard behaviour is usually handled by swipe gestures or UI buttons.
    // We do not intercept system gestures here for simplicity in this task.
}
