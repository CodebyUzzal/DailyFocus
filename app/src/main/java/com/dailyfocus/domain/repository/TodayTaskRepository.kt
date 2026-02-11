package com.dailyfocus.domain.repository

import com.dailyfocus.domain.model.TaskItem
import com.dailyfocus.domain.model.TodayTask
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Repository contract for one-time [TodayTask]s and their [TaskItem] children. */
interface TodayTaskRepository {
    fun getTasksByDate(date: LocalDate): Flow<List<TodayTask>>
    fun getTaskItemsByParent(parentTaskId: Long): Flow<List<TaskItem>>
    suspend fun insertTask(task: TodayTask): Long
    suspend fun updateTask(task: TodayTask)
    suspend fun deleteTask(id: Long)
    suspend fun insertTaskItem(item: TaskItem): Long
    suspend fun updateTaskItem(item: TaskItem)
    suspend fun deleteTaskItem(id: Long)
    suspend fun getTaskItemsByParentOnce(parentTaskId: Long): List<TaskItem>
}
