package com.dailyfocus.data.local.database

import androidx.room.TypeConverter
import com.dailyfocus.domain.model.GoalType
import com.dailyfocus.domain.model.TaskCategory
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Room TypeConverters for non-primitive types used across entities.
 * All date/time values are stored as ISO-8601 strings for readability and portability.
 */
class Converters {

    // ── LocalDate ───────────────────────────────────────────────────────

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    // ── LocalDateTime ───────────────────────────────────────────────────

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? = dateTime?.toString()

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }

    // ── TaskCategory ────────────────────────────────────────────────────

    @TypeConverter
    fun fromTaskCategory(category: TaskCategory?): String? = category?.name

    @TypeConverter
    fun toTaskCategory(value: String?): TaskCategory? =
        value?.let { runCatching { TaskCategory.valueOf(it) }.getOrNull() }

    // ── GoalType ────────────────────────────────────────────────────────

    @TypeConverter
    fun fromGoalType(type: GoalType?): String? = type?.name

    @TypeConverter
    fun toGoalType(value: String?): GoalType? =
        value?.let { runCatching { GoalType.valueOf(it) }.getOrNull() }

    // ── Set<DayOfWeek> ──────────────────────────────────────────────────
    // Stored as uppercase comma-separated string: "MONDAY,WEDNESDAY,FRIDAY"
    // Safe parsing: handles empty strings, trailing commas, malformed entries.

    @TypeConverter
    fun fromDayOfWeekSet(days: Set<DayOfWeek>?): String? {
        if (days == null || days.isEmpty()) return null
        return days.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toDayOfWeekSet(value: String?): Set<DayOfWeek> {
        if (value.isNullOrBlank()) return emptySet()
        return value.split(",")
            .map { it.trim().uppercase() }
            .filter { it.isNotBlank() }
            .mapNotNull { name ->
                runCatching { DayOfWeek.valueOf(name) }.getOrNull()
            }
            .toSet()
    }
}
