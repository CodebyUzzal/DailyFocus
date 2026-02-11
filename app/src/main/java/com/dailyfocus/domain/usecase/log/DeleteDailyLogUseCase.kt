package com.dailyfocus.domain.usecase.log

import com.dailyfocus.domain.repository.DailyLogRepository
import javax.inject.Inject

/**
 * Deletes a daily log entry by ID.
 */
class DeleteDailyLogUseCase @Inject constructor(
    private val repository: DailyLogRepository
) {
    suspend operator fun invoke(id: Long) = repository.delete(id)
}
