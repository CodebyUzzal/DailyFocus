package com.dailyfocus.core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/** Extension to create a single DataStore instance tied to the application context. */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dailyfocus_prefs")

/**
 * Lightweight app-level preferences backed by DataStore.
 * Stores only non-relational state that doesn't belong in Room:
 * - Last opened date (for day boundary detection)
 * - Category filter selection
 * - Theme mode preference
 */
@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val LAST_OPEN_DATE = stringPreferencesKey("last_open_date")
        val CATEGORY_FILTER = stringPreferencesKey("category_filter")
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    // ── Last Open Date ──────────────────────────────────────────────────

    /** Observe the last date the app was opened. */
    val lastOpenDate: Flow<LocalDate?> = context.dataStore.data.map { prefs ->
        prefs[Keys.LAST_OPEN_DATE]?.let { LocalDate.parse(it) }
    }

    /** Update the last open date to [date]. Called by DailyBoundaryManager. */
    suspend fun setLastOpenDate(date: LocalDate) {
        context.dataStore.edit { prefs ->
            prefs[Keys.LAST_OPEN_DATE] = date.toString()
        }
    }

    // ── Category Filter ─────────────────────────────────────────────────

    /** Observe the persisted category filter (null = show all). */
    val categoryFilter: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[Keys.CATEGORY_FILTER]
    }

    /** Persist the selected category filter. Pass null to clear. */
    suspend fun setCategoryFilter(category: String?) {
        context.dataStore.edit { prefs ->
            if (category != null) {
                prefs[Keys.CATEGORY_FILTER] = category
            } else {
                prefs.remove(Keys.CATEGORY_FILTER)
            }
        }
    }

    // ── Theme Mode ──────────────────────────────────────────────────────

    /** Observe the theme mode: "system", "light", or "dark". */
    val themeMode: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.THEME_MODE] ?: THEME_SYSTEM
    }

    /** Persist the theme mode selection. */
    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.THEME_MODE] = mode
        }
    }

    companion object {
        const val THEME_SYSTEM = "system"
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
    }
}
