package com.dailyfocus.domain.usecase.habits

import com.dailyfocus.domain.model.HabitLog
import com.dailyfocus.domain.repository.HabitRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * Toggles a habit's completion for a specific date.
 * If a log exists, it is removed. If not, a log is created.
 */
class ToggleHabitForDateUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habitId: Long, date: LocalDate) {
        val hasLog = repository.hasLogForDate(habitId, date)
        if (hasLog) {
            repository.deleteLog(habitId, date)
        } else {
            repository.insertLog(HabitLog(habitId = habitId, date = date))
        }
    }
}
