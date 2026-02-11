package com.dailyfocus.data.local.database

import androidx.room.TypeConverter
import com.dailyfocus.domain.model.GoalType
import com.dailyfocus.domain.model.HabitFrequency
import com.dailyfocus.domain.model.TaskCategory
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
    fun toTaskCategory(value: String?): TaskCategory? = value?.let { TaskCategory.valueOf(it) }

    // ── GoalType ────────────────────────────────────────────────────────

    @TypeConverter
    fun fromGoalType(type: GoalType?): String? = type?.name

    @TypeConverter
    fun toGoalType(value: String?): GoalType? = value?.let { GoalType.valueOf(it) }

    // ── HabitFrequency ──────────────────────────────────────────────────

    @TypeConverter
    fun fromHabitFrequency(frequency: HabitFrequency?): String? = frequency?.toStorageString()

    @TypeConverter
    fun toHabitFrequency(value: String?): HabitFrequency? =
        value?.let { HabitFrequency.fromStorageString(it) }
}
