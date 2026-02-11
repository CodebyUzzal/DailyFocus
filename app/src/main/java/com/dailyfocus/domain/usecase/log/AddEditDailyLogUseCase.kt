package com.dailyfocus.domain.usecase.log

import com.dailyfocus.domain.model.DailyLogEntry
import com.dailyfocus.domain.repository.DailyLogRepository
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Adds or updates a [DailyLogEntry].
 */
class AddEditDailyLogUseCase @Inject constructor(
    private val repository: DailyLogRepository
) {
    suspend operator fun invoke(entry: DailyLogEntry) {
        if (entry.id == 0L) {
            repository.insert(entry)
        } else {
            repository.update(entry.copy(updatedAt = LocalDateTime.now()))
        }
    }
}
