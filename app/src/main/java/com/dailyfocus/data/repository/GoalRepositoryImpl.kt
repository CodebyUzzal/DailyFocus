package com.dailyfocus.data.repository

import com.dailyfocus.data.local.dao.GoalDao
import com.dailyfocus.data.local.dao.GoalItemDao
import com.dailyfocus.data.local.entity.GoalEntity
import com.dailyfocus.data.local.entity.GoalItemEntity
import com.dailyfocus.domain.model.Goal
import com.dailyfocus.domain.model.GoalItem
import com.dailyfocus.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao,
    private val itemDao: GoalItemDao
) : GoalRepository {

    override fun getAllGoals(): Flow<List<Goal>> =
        goalDao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getGoalById(id: Long): Goal? =
        goalDao.getById(id)?.toDomain()

    override suspend fun insertGoal(goal: Goal): Long =
        goalDao.insert(goal.toEntity())

    override suspend fun updateGoal(goal: Goal) =
        goalDao.update(goal.toEntity())

    override suspend fun deleteGoal(id: Long) =
        goalDao.delete(id)

    override fun getItemsByGoal(goalId: Long): Flow<List<GoalItem>> =
        itemDao.getByGoal(goalId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertGoalItem(item: GoalItem): Long =
        itemDao.insert(item.toEntity())

    override suspend fun updateGoalItem(item: GoalItem) =
        itemDao.update(item.toEntity())

    override suspend fun deleteGoalItem(id: Long) =
        itemDao.delete(id)
}

private fun GoalEntity.toDomain() = Goal(
    id = id, title = title, type = type, deadline = deadline, createdAt = createdAt
)

private fun Goal.toEntity() = GoalEntity(
    id = id, title = title, type = type, deadline = deadline, createdAt = createdAt
)

private fun GoalItemEntity.toDomain() = GoalItem(
    id = id, goalId = goalId, title = title,
    isCompleted = isCompleted, createdAt = createdAt
)

private fun GoalItem.toEntity() = GoalItemEntity(
    id = id, goalId = goalId, title = title,
    isCompleted = isCompleted, createdAt = createdAt
)
