package org.example.project.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun AdaptiveBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
)
