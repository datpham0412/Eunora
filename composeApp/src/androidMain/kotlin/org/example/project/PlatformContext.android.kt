package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.shared.db.MoodDatabase
import database.DatabaseDriverFactory

@Composable
actual fun getPlatformContext(): Any {
    return LocalContext.current
}

actual fun createMoodDatabase(context: Any): MoodDatabase {
    val androidContext = context as android.content.Context
    val driverFactory = DatabaseDriverFactory(androidContext)
    return MoodDatabase(driverFactory.createDriver())
}
