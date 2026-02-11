package com.dailyfocus.core.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Centralized date utilities for DailyFocus.
 * All dates are stored and compared as ISO-8601 strings (yyyy-MM-dd).
 */
object DateUtils {

    private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    /** Returns today's date using the device's default timezone. */
    fun today(): LocalDate = LocalDate.now()

    /** Formats a [LocalDate] to ISO string (yyyy-MM-dd). */
    fun formatDate(date: LocalDate): String = date.format(DATE_FORMATTER)

    /** Parses an ISO date string back to [LocalDate]. */
    fun parseDate(dateString: String): LocalDate = LocalDate.parse(dateString, DATE_FORMATTER)

    /** Formats a [LocalDateTime] to ISO string. */
    fun formatDateTime(dateTime: LocalDateTime): String = dateTime.format(DATE_TIME_FORMATTER)

    /** Parses an ISO datetime string back to [LocalDateTime]. */
    fun parseDateTime(dateTimeString: String): LocalDateTime =
        LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER)

    /**
     * Computes the current streak length given a sorted list of completed dates (ascending).
     * Streak counts backward from [today] — each consecutive prior day extends the streak.
     *
     * @param completedDates sorted ascending list of dates when the item was completed
     * @param today reference date (defaults to actual today)
     * @return number of consecutive days ending at or before [today]
     */
    fun computeStreak(completedDates: List<LocalDate>, today: LocalDate = today()): Int {
        if (completedDates.isEmpty()) return 0

        var streak = 0
        var checkDate = today

        // Walk backwards from today looking for consecutive completions
        val dateSet = completedDates.toHashSet()
        while (dateSet.contains(checkDate)) {
            streak++
            checkDate = checkDate.minusDays(1)
        }

        return streak
    }

    /**
     * Computes the longest streak from a list of completed dates.
     *
     * @param completedDates list of dates (need not be sorted)
     * @return length of the longest consecutive run
     */
    fun computeLongestStreak(completedDates: List<LocalDate>): Int {
        if (completedDates.isEmpty()) return 0

        val sorted = completedDates.sorted()
        var longest = 1
        var current = 1

        for (i in 1 until sorted.size) {
            val daysBetween = ChronoUnit.DAYS.between(sorted[i - 1], sorted[i])
            when {
                daysBetween == 1L -> {
                    current++
                    if (current > longest) longest = current
                }
                daysBetween > 1L -> current = 1
                // daysBetween == 0 means duplicate date, skip
            }
        }

        return longest
    }

    /**
     * Returns all dates between [startInclusive] and [endInclusive].
     * Useful for generating missing DailyTaskInstances.
     */
    fun dateRange(startInclusive: LocalDate, endInclusive: LocalDate): List<LocalDate> {
        val dates = mutableListOf<LocalDate>()
        var current = startInclusive
        while (!current.isAfter(endInclusive)) {
            dates.add(current)
            current = current.plusDays(1)
        }
        return dates
    }
}
