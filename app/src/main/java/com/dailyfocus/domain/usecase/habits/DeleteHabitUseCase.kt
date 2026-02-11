package com.dailyfocus.domain.usecase.habits

import com.dailyfocus.domain.repository.HabitRepository
import javax.inject.Inject

/**
 * Deletes a habit and its associated logs.
 * Provides clean architecture compliance — replaces direct repo access in ViewModel.
 */
class DeleteHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habitId: Long) {
        repository.deleteHabit(habitId)
    }
}
