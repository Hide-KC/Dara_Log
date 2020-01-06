package work.kcs_labo.dara_log.domain.usecase

import work.kcs_labo.dara_log.domain.entity.CommittedTaskEntity

interface RegisterCommittedTasksUseCase {
  suspend fun registerCommittedTasks(committed: List<CommittedTaskEntity>)
}