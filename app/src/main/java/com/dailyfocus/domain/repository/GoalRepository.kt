package com.dailyfocus.domain.repository

import com.dailyfocus.domain.model.Goal
import com.dailyfocus.domain.model.GoalItem
import kotlinx.coroutines.flow.Flow

/** Repository contract for [Goal]s and their [GoalItem] checklists. */
interface GoalRepository {
    fun getAllGoals(): Flow<List<Goal>>
    suspend fun getGoalById(id: Long): Goal?
    suspend fun insertGoal(goal: Goal): Long
    suspend fun updateGoal(goal: Goal)
    suspend fun deleteGoal(id: Long)
    fun getItemsByGoal(goalId: Long): Flow<List<GoalItem>>
    suspend fun insertGoalItem(item: GoalItem): Long
    suspend fun updateGoalItem(item: GoalItem)
    suspend fun deleteGoalItem(id: Long)
}
