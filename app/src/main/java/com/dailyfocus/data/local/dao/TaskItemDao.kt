package com.dailyfocus.data.local.dao

import androidx.room.*
import com.dailyfocus.data.local.entity.TaskItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskItemDao {

    @Query("SELECT * FROM task_items WHERE parentTaskId = :parentId ORDER BY position ASC")
    fun getByParent(parentId: Long): Flow<List<TaskItemEntity>>

    @Query("SELECT * FROM task_items WHERE parentTaskId = :parentId ORDER BY position ASC")
    suspend fun getByParentOnce(parentId: Long): List<TaskItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TaskItemEntity): Long

    @Update
    suspend fun update(item: TaskItemEntity)

    @Query("DELETE FROM task_items WHERE id = :id")
    suspend fun delete(id: Long)
}
