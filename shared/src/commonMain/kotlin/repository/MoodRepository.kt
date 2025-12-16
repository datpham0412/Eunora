package repository

import ai.AIService
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.shared.db.MoodDatabase
import com.example.shared.db.Mood_entries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.MoodEntry
import model.MoodArtMetadata
import model.MoodAIResult
import model.NormalizedMood
import model.classifyMood
import kotlin.random.Random
import util.currentTimeMillis

class MoodRepository(
    private val aiService: AIService,
    private val database: MoodDatabase
) {
    private val json = Json { prettyPrint = false }

    suspend fun analyzeMood(rawMoodText: String): MoodEntry {
        // Call AI Service to interpret mood
        val aiResult = aiService.interpretMood(rawMoodText)

        // Classify mood deterministically from emotion scores
        val normalizedMood = classifyMood(aiResult.emotion)

        val entry = MoodEntry(
            id = generateId(),
            rawMoodText = rawMoodText,
            normalizedMood = normalizedMood,
            ai = aiResult,
            art = MoodArtMetadata(
                mood = normalizedMood,
                assetId = null  // Will be assigned later from local asset library
            ),
            timestamp = currentTimeMillis()
        )

        // Auto-save to database
        saveMoodEntry(entry)

        return entry
    }

    suspend fun saveMoodEntry(entry: MoodEntry) {
        database.moodEntriesQueries.insert(
            id = entry.id,
            raw_mood_text = entry.rawMoodText,
            normalized_mood = entry.normalizedMood.name,
            ai_result_json = json.encodeToString(entry.ai),
            art_metadata_json = json.encodeToString(entry.art),
            timestamp = entry.timestamp
        )
    }

    fun getAllMoodEntries(): Flow<List<MoodEntry>> {
        return database.moodEntriesQueries
            .selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { rows -> rows.map { it.toMoodEntry() } }
    }

    suspend fun getMoodEntryById(id: String): MoodEntry? {
        return database.moodEntriesQueries
            .selectById(id)
            .executeAsOneOrNull()
            ?.toMoodEntry()
    }

    suspend fun deleteMoodEntry(id: String) {
        database.moodEntriesQueries.deleteById(id)
    }

    suspend fun deleteAllMoodEntries() {
        database.moodEntriesQueries.deleteAll()
    }

    suspend fun getMoodEntriesCount(): Long {
        return database.moodEntriesQueries
            .count()
            .executeAsOne()
    }

    private fun Mood_entries.toMoodEntry(): MoodEntry {
        return MoodEntry(
            id = id,
            rawMoodText = raw_mood_text,
            normalizedMood = NormalizedMood.valueOf(normalized_mood),
            ai = json.decodeFromString<MoodAIResult>(ai_result_json),
            art = json.decodeFromString<MoodArtMetadata>(art_metadata_json),
            timestamp = timestamp
        )
    }

    private fun generateId(): String {
        val timestamp = currentTimeMillis()
        val random = Random.nextInt(10000)
        return "mood_${timestamp}_${random}"
    }
}
