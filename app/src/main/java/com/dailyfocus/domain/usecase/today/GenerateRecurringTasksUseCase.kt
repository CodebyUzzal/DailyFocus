package com.dailyfocus.domain.usecase.today

import com.dailyfocus.core.util.DateUtils
import com.dailyfocus.domain.model.RecurringTask
import com.dailyfocus.domain.model.TodayTask
import com.dailyfocus.domain.repository.RecurringTaskRepository
import com.dailyfocus.domain.repository.TodayTaskRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject

/**
 * Generates [TodayTask] entries from active [RecurringTask] definitions
 * when the current day matches one of the configured weekdays.
 *
 * Deduplication: if a task was already generated for this recurring task
 * and today's date, it is NOT re-inserted.
 *
 * Call this on app start, on date change, and after creating a new recurring task.
 */
class GenerateRecurringTasksUseCase @Inject constructor(
    private val recurringTaskRepository: RecurringTaskRepository,
    private val todayTaskRepository: TodayTaskRepository
) {
    suspend operator fun invoke(date: LocalDate = DateUtils.today()) {
        val activeTasks = recurringTaskRepository.getAllActive().first()
        val currentDayOfWeek = date.dayOfWeek

        activeTasks.forEach { recurring ->
            // If daysOfWeek is empty → applies every day
            val shouldGenerate = recurring.daysOfWeek.isEmpty() ||
                    recurring.daysOfWeek.contains(currentDayOfWeek)

            if (shouldGenerate) {
                val alreadyExists = todayTaskRepository
                    .existsForRecurringTaskAndDate(recurring.id, date)

                if (!alreadyExists) {
                    todayTaskRepository.insert(
                        TodayTask(
                            title = recurring.title,
                            category = recurring.category,
                            date = date,
                            recurringTaskId = recurring.id
                        )
                    )
                }
            }
        }
    }
}
