package com.dailyfocus.domain.usecase.today

import com.dailyfocus.domain.model.Routine
import com.dailyfocus.domain.repository.RoutineRepository
import javax.inject.Inject

/**
 * CRUD operations for Routines (formerly Recurring Tasks).
 */
class ManageRoutinesUseCase @Inject constructor(
    private val repository: RoutineRepository
) {
    suspend fun addRoutine(routine: Routine): Long {
        return repository.insert(routine)
    }

    suspend fun updateRoutine(routine: Routine) {
        repository.update(routine)
    }

    suspend fun deleteRoutine(id: Long) {
        repository.delete(id)
    }

    fun getAll(): kotlinx.coroutines.flow.Flow<List<Routine>> {
        return repository.getAll()
    }
}
