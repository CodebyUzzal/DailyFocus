package com.dailyfocus.domain.usecase.today

import com.dailyfocus.core.preferences.AppPreferences
import com.dailyfocus.core.util.DateUtils
import com.dailyfocus.domain.model.DailyTaskInstance
import com.dailyfocus.domain.model.Routine
import com.dailyfocus.domain.repository.DailyTaskInstanceRepository
import com.dailyfocus.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject

/**
 * Handles day boundary transitions without relying on background jobs.
 *
 * On each app resume, this use case checks whether the day has changed since
 * the last time the app was opened. If so, it generates [DailyTaskInstance]
 * records for all active [RecurringTask]s for every missed date (including today).
 *
 * This ensures:
 * - Streaks are computed correctly even if the app wasn't open at midnight
 * - The Today screen always has fresh instances for the current day
 */
class DailyBoundaryManager @Inject constructor(
    private val preferences: AppPreferences,
    private val routineRepository: RoutineRepository,
    private val dailyTaskInstanceRepository: DailyTaskInstanceRepository
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
            generateMissingInstances(startDate, today)
            preferences.setLastOpenDate(today)
        }
    }

    /**
     * Generates [DailyTaskInstance] records for each active recurring task
     * on each date in the range [from, to] (inclusive), skipping dates that
     * already have instances (idempotent).
     */
    private suspend fun generateMissingInstances(from: LocalDate, to: LocalDate) {
        val activeTasks = routineRepository.getAllActive().first()
        if (activeTasks.isEmpty()) return

        val datesToFill = DateUtils.dateRange(from, to)
        val instancesToInsert = mutableListOf<DailyTaskInstance>()

        for (date in datesToFill) {
            for (task in activeTasks) {
                val exists = dailyTaskInstanceRepository.existsForDate(task.id, date)
                if (!exists) {
                    instancesToInsert.add(
                        DailyTaskInstance(
                            recurringTaskId = task.id,
                            date = date,
                            title = task.title,
                            category = task.category
                        )
                    )
                }
            }
        }

        if (instancesToInsert.isNotEmpty()) {
            dailyTaskInstanceRepository.insertAll(instancesToInsert)
        }
    }
}
