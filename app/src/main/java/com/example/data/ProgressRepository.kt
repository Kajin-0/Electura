package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.AttemptRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.gameDataStore: DataStore<Preferences> by preferencesDataStore(name = "ac_circuit_progress")

data class PersistedProgress(
    val solvedIds: Set<String> = emptySet(),
    val totalXp: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val totalAttempts: Int = 0,
    val totalCorrect: Int = 0,
    val drillsSolved: Int = 0,
    val showQuickKeypad: Boolean = true,
    val recentAttempts: List<AttemptRecord> = emptyList()
)

class ProgressRepository(private val context: Context) {

    private object Keys {
        val SOLVED_IDS = stringSetPreferencesKey("solved_ids")
        val TOTAL_XP = intPreferencesKey("total_xp")
        val CURRENT_STREAK = intPreferencesKey("current_streak")
        val BEST_STREAK = intPreferencesKey("best_streak")
        val TOTAL_ATTEMPTS = intPreferencesKey("total_attempts")
        val TOTAL_CORRECT = intPreferencesKey("total_correct")
        val DRILLS_SOLVED = intPreferencesKey("drills_solved")
        val SHOW_QUICK_KEYPAD = booleanPreferencesKey("show_quick_keypad")
        val RECENT_ATTEMPTS = stringPreferencesKey("recent_attempts")
    }

    val progressFlow: Flow<PersistedProgress> = context.gameDataStore.data.map { prefs ->
        PersistedProgress(
            solvedIds = prefs[Keys.SOLVED_IDS] ?: emptySet(),
            totalXp = prefs[Keys.TOTAL_XP] ?: 0,
            currentStreak = prefs[Keys.CURRENT_STREAK] ?: 0,
            bestStreak = prefs[Keys.BEST_STREAK] ?: 0,
            totalAttempts = prefs[Keys.TOTAL_ATTEMPTS] ?: 0,
            totalCorrect = prefs[Keys.TOTAL_CORRECT] ?: 0,
            drillsSolved = prefs[Keys.DRILLS_SOLVED] ?: 0,
            showQuickKeypad = prefs[Keys.SHOW_QUICK_KEYPAD] ?: true,
            recentAttempts = prefs[Keys.RECENT_ATTEMPTS]?.let(ProgressCodec::decodeAttempts) ?: emptyList()
        )
    }

    suspend fun recordCorrectSubmission(
        attempt: AttemptRecord,
        isProcedural: Boolean,
        xpEarned: Int
    ) {
        context.gameDataStore.edit { prefs ->
            val currentSolved = prefs[Keys.SOLVED_IDS] ?: emptySet()
            val alreadySolved = !isProcedural && currentSolved.contains(attempt.problemId)
            val newStreak = (prefs[Keys.CURRENT_STREAK] ?: 0) + 1
            val best = maxOf(prefs[Keys.BEST_STREAK] ?: 0, newStreak)

            if (!isProcedural) {
                prefs[Keys.SOLVED_IDS] = currentSolved + attempt.problemId
            } else {
                prefs[Keys.DRILLS_SOLVED] = (prefs[Keys.DRILLS_SOLVED] ?: 0) + 1
            }

            // Award full XP on first solve or procedural drill; 25 practice XP on re-solve
            val awardedXp = if (alreadySolved) 25 else xpEarned
            prefs[Keys.TOTAL_XP] = (prefs[Keys.TOTAL_XP] ?: 0) + awardedXp
            prefs[Keys.CURRENT_STREAK] = newStreak
            prefs[Keys.BEST_STREAK] = best
            prefs[Keys.TOTAL_ATTEMPTS] = (prefs[Keys.TOTAL_ATTEMPTS] ?: 0) + 1
            prefs[Keys.TOTAL_CORRECT] = (prefs[Keys.TOTAL_CORRECT] ?: 0) + 1

            val existing = prefs[Keys.RECENT_ATTEMPTS]?.let(ProgressCodec::decodeAttempts) ?: emptyList()
            val updatedAttempts = (listOf(attempt.copy(xpEarned = awardedXp)) + existing).take(30)
            prefs[Keys.RECENT_ATTEMPTS] = ProgressCodec.encodeAttempts(updatedAttempts)
        }
    }

    suspend fun recordIncorrectSubmission(attempt: AttemptRecord) {
        context.gameDataStore.edit { prefs ->
            prefs[Keys.CURRENT_STREAK] = 0
            prefs[Keys.TOTAL_ATTEMPTS] = (prefs[Keys.TOTAL_ATTEMPTS] ?: 0) + 1

            val existing = prefs[Keys.RECENT_ATTEMPTS]?.let(ProgressCodec::decodeAttempts) ?: emptyList()
            val updatedAttempts = (listOf(attempt) + existing).take(30)
            prefs[Keys.RECENT_ATTEMPTS] = ProgressCodec.encodeAttempts(updatedAttempts)
        }
    }

    suspend fun setShowQuickKeypad(enabled: Boolean) {
        context.gameDataStore.edit { prefs ->
            prefs[Keys.SHOW_QUICK_KEYPAD] = enabled
        }
    }

    suspend fun resetAllProgress() {
        context.gameDataStore.edit { prefs ->
            val keypadPref = prefs[Keys.SHOW_QUICK_KEYPAD] ?: true
            prefs.clear()
            prefs[Keys.SHOW_QUICK_KEYPAD] = keypadPref
        }
    }
}
