package com.dailyfocus.domain.model

/**
 * Frequency at which a habit should be performed.
 * - [Daily] — every single day
 * - [CustomWeekly] — a target number of days per week (flexible scheduling)
 */
sealed class HabitFrequency {
    data object Daily : HabitFrequency()
    data class CustomWeekly(val daysPerWeek: Int) : HabitFrequency() {
        init {
            require(daysPerWeek in 1..7) { "daysPerWeek must be between 1 and 7" }
        }
    }

    /** Serialize to a storable string representation. */
    fun toStorageString(): String = when (this) {
        is Daily -> "daily"
        is CustomWeekly -> "weekly:$daysPerWeek"
    }

    companion object {
        /** Deserialize from storage string. */
        fun fromStorageString(value: String): HabitFrequency = when {
            value == "daily" -> Daily
            value.startsWith("weekly:") -> {
                val days = value.removePrefix("weekly:").toInt()
                CustomWeekly(days)
            }
            else -> Daily // safe default
        }
    }
}
