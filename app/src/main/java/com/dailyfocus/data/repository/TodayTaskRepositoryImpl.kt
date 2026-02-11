package com.dailyfocus.data.repository

import com.dailyfocus.data.local.dao.TaskItemDao
import com.dailyfocus.data.local.dao.TodayTaskDao
import com.dailyfocus.data.local.entity.TaskItemEntity
import com.dailyfocus.data.local.entity.TodayTaskEntity
import com.dailyfocus.domain.model.TaskItem
import com.dailyfocus.domain.model.TodayTask
import com.dailyfocus.domain.repository.TodayTaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class TodayTaskRepositoryImpl @Inject constructor(
    private val taskDao: TodayTaskDao,
    private val itemDao: TaskItemDao
) : TodayTaskRepository {

    override fun getTasksByDate(date: LocalDate): Flow<List<TodayTask>> =
        taskDao.getByDate(date).map { list -> list.map { it.toDomain() } }

    override fun getTaskItemsByParent(parentTaskId: Long): Flow<List<TaskItem>> =
        itemDao.getByParent(parentTaskId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertTask(task: TodayTask): Long =
        taskDao.insert(task.toEntity())

    override suspend fun updateTask(task: TodayTask) =
        taskDao.update(task.toEntity())

    override suspend fun deleteTask(id: Long) =
        taskDao.delete(id)

    override suspend fun insertTaskItem(item: TaskItem): Long =
        itemDao.insert(item.toEntity())

    override suspend fun updateTaskItem(item: TaskItem) =
        itemDao.update(item.toEntity())

    override suspend fun deleteTaskItem(id: Long) =
        itemDao.delete(id)

    override suspend fun getTaskItemsByParentOnce(parentTaskId: Long): List<TaskItem> =
        itemDao.getByParentOnce(parentTaskId).map { it.toDomain() }
}

// ── Mappers ─────────────────────────────────────────────────────────────

private fun TodayTaskEntity.toDomain() = TodayTask(
    id = id, title = title, category = category, date = date,
    isCompleted = isCompleted, position = position,
    createdAt = createdAt, updatedAt = updatedAt
)

private fun TodayTask.toEntity() = TodayTaskEntity(
    id = id, title = title, category = category, date = date,
    isCompleted = isCompleted, position = position,
    createdAt = createdAt, updatedAt = updatedAt
)

private fun TaskItemEntity.toDomain() = TaskItem(
    id = id, parentTaskId = parentTaskId, title = title,
    isCompleted = isCompleted, position = position,
    createdAt = createdAt, updatedAt = updatedAt
)

private fun TaskItem.toEntity() = TaskItemEntity(
    id = id, parentTaskId = parentTaskId, title = title,
    isCompleted = isCompleted, position = position,
    createdAt = createdAt, updatedAt = updatedAt
)
