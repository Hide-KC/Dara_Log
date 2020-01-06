package work.kcs_labo.dara_log.domain.usecase

import work.kcs_labo.dara_log.domain.entity.CommittedTaskEntity
import java.util.*

interface GetCommittedTasksUseCase {
  suspend fun getCommittedTasks(date: Calendar): List<CommittedTaskEntity>
}