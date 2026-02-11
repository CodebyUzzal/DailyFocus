package com.dailyfocus.di

import android.content.Context
import androidx.room.Room
import com.dailyfocus.data.local.dao.*
import com.dailyfocus.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing the Room database singleton and all DAO instances.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "dailyfocus.db"
        )
        .addMigrations(AppDatabase.MIGRATION_1_2)
        .build()
    }

    @Provides fun provideRoutineDao(db: AppDatabase): RoutineDao = db.routineDao()
    @Provides fun provideDailyTaskInstanceDao(db: AppDatabase): DailyTaskInstanceDao = db.dailyTaskInstanceDao()
    @Provides fun provideTodayTaskDao(db: AppDatabase): TodayTaskDao = db.todayTaskDao()
    @Provides fun provideTaskItemDao(db: AppDatabase): TaskItemDao = db.taskItemDao()
    @Provides fun provideHabitDao(db: AppDatabase): HabitDao = db.habitDao()
    @Provides fun provideHabitLogDao(db: AppDatabase): HabitLogDao = db.habitLogDao()
    @Provides fun provideGoalDao(db: AppDatabase): GoalDao = db.goalDao()
    @Provides fun provideGoalItemDao(db: AppDatabase): GoalItemDao = db.goalItemDao()
    @Provides fun provideDailyLogEntryDao(db: AppDatabase): DailyLogEntryDao = db.dailyLogEntryDao()
}
