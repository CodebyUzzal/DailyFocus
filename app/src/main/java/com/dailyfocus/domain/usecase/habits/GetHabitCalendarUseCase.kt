package com.dailyfocus.domain.usecase.habits

import com.dailyfocus.domain.repository.HabitRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * Data class representing a single day in the habit calendar.
 */
data class HabitCalendarDay(
    val date: LocalDate,
    val status: HabitDayStatus
)

enum class HabitDayStatus {
    DONE,
    MISSED,
    TODAY,
    FUTURE,
    INACTIVE
}

/**
 * Builds calendar data for a habit's heatmap display.
 * Shows the past N days with done/missed/today status.
 */
class GetHabitCalendarUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    /**
     * @param habitId the habit to build the calendar for
     * @param startDate first date in the calendar window
     * @param endDate last date in the calendar window
     * @return list of [HabitCalendarDay] for the date range
     */
    suspend operator fun invoke(
        habitId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<HabitCalendarDay> {
        val logDates = repository.getLogDatesForHabit(habitId).toHashSet()
        val today = LocalDate.now()
        val days = mutableListOf<HabitCalendarDay>()

        var current = startDate
        while (!current.isAfter(endDate)) {
            val status = when {
                current == today -> HabitDayStatus.TODAY
                current.isAfter(today) -> HabitDayStatus.FUTURE
                logDates.contains(current) -> HabitDayStatus.DONE
                else -> HabitDayStatus.MISSED
            }
            days.add(HabitCalendarDay(date = current, status = status))
            current = current.plusDays(1)
        }

        return days
    }
}
