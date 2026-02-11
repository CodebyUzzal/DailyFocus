package com.dailyfocus.data.local.dao

import androidx.room.*
import com.dailyfocus.data.local.entity.GoalItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalItemDao {

    @Query("SELECT * FROM goal_items WHERE goalId = :goalId ORDER BY position ASC")
    fun getByGoal(goalId: Long): Flow<List<GoalItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GoalItemEntity): Long

    @Update
    suspend fun update(item: GoalItemEntity)

    @Query("DELETE FROM goal_items WHERE id = :id")
    suspend fun delete(id: Long)
}
