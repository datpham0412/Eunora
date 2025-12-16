package org.example.project

import androidx.compose.runtime.Composable
import com.example.shared.db.MoodDatabase
import database.DatabaseDriverFactory

@Composable
actual fun getPlatformContext(): Any {
    return Unit // iOS doesn't need context for SQLDelight
}

actual fun createMoodDatabase(context: Any): MoodDatabase {
    val driverFactory = DatabaseDriverFactory()
    return MoodDatabase(driverFactory.createDriver())
}
