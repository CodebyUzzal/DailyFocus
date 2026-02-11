package com.dailyfocus.domain.usecase.today

import com.dailyfocus.core.preferences.AppPreferences
import com.dailyfocus.core.util.DateUtils
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject

/**
 * Handles day boundary transitions without relying on background jobs.
 *
 * On each app resume, checks whether the day has changed since the last open.
 * If so, generates recurring task instances for every missed date (including today)
 * via [GenerateRecurringTasksUseCase].
 */
class DailyBoundaryManager @Inject constructor(
    private val preferences: AppPreferences,
    private val generateRecurringTasks: GenerateRecurringTasksUseCase
) {
    /**
     * Call this on every app resume (e.g. from LaunchedEffect in the main screen).
     * Generates missing instances and updates the last-open-date preference.
     */
    suspend fun onAppResume() {
        val today = DateUtils.today()
        val lastOpenDate = preferences.lastOpenDate.first()

        if (lastOpenDate == null || lastOpenDate < today) {
            val startDate = lastOpenDate?.plusDays(1) ?: today
            val datesToFill = DateUtils.dateRange(startDate, today)

            for (date in datesToFill) {
                generateRecurringTasks(date)
            }

            preferences.setLastOpenDate(today)
        } else {
            // Same day — still generate in case new recurring tasks were added
            generateRecurringTasks(today)
        }
    }
}
