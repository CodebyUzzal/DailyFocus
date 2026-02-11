package com.dailyfocus.domain.usecase.today

import com.dailyfocus.core.util.DateUtils
import com.dailyfocus.domain.model.TaskCategory
import com.dailyfocus.domain.model.TodayTask
import com.dailyfocus.domain.repository.TodayTaskRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * Adds a new manual task to today's task list.
 */
class AddTodayTaskUseCase @Inject constructor(
    private val repository: TodayTaskRepository
) {
    suspend operator fun invoke(
        title: String,
        category: TaskCategory = TaskCategory.PERSONAL,
        date: LocalDate = DateUtils.today()
    ): Long {
        return repository.insert(
            TodayTask(
                title = title,
                category = category,
                date = date
            )
        )
    }
}
