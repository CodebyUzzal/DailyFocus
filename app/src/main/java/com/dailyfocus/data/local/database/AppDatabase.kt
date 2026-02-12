package com.dailyfocus.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dailyfocus.data.local.dao.*
import com.dailyfocus.data.local.entity.*

/**
 * Room database for DailyFocus.
 * Uses fallbackToDestructiveMigration — no real users exist yet.
 */
@Database(
    entities = [
        RecurringTaskEntity::class,
        TodayTaskEntity::class,
        HabitEntity::class,
        HabitLogEntity::class,
        GoalEntity::class,
        GoalItemEntity::class,
        DailyLogEntryEntity::class
    ],
    version = 4,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recurringTaskDao(): RecurringTaskDao
    abstract fun todayTaskDao(): TodayTaskDao
    abstract fun habitDao(): HabitDao
    abstract fun habitLogDao(): HabitLogDao
    abstract fun goalDao(): GoalDao
    abstract fun goalItemDao(): GoalItemDao
    abstract fun dailyLogEntryDao(): DailyLogEntryDao
}
