package com.dailyfocus.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dailyfocus.data.local.dao.*
import com.dailyfocus.data.local.entity.*

/**
 * Room database for DailyFocus.
 * All tables are defined here with TypeConverters for custom types.
 */
@Database(
    entities = [
        RoutineEntity::class,
        DailyTaskInstanceEntity::class,
        TodayTaskEntity::class,
        TaskItemEntity::class,
        HabitEntity::class,
        HabitLogEntity::class,
        GoalEntity::class,
        GoalItemEntity::class,
        DailyLogEntryEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
    abstract fun dailyTaskInstanceDao(): DailyTaskInstanceDao
    abstract fun todayTaskDao(): TodayTaskDao
    abstract fun taskItemDao(): TaskItemDao
    abstract fun habitDao(): HabitDao
    abstract fun habitLogDao(): HabitLogDao
    abstract fun goalDao(): GoalDao
    abstract fun goalItemDao(): GoalItemDao
    abstract fun dailyLogEntryDao(): DailyLogEntryDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE recurring_tasks ADD COLUMN currentStreak INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE recurring_tasks ADD COLUMN longestStreak INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE recurring_tasks ADD COLUMN lastCompletedDate TEXT")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_recurring_tasks_lastCompletedDate ON recurring_tasks(lastCompletedDate)")
            }
        }
    }
}
