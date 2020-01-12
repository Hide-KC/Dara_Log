package work.kcs_labo.dara_log.domain.usecase

import work.kcs_labo.dara_log.domain.entity.CommittedEntity
import java.util.*

interface CommittedEntityUseCase {
  suspend fun getCommittedEntities(date: Calendar): List<CommittedEntity>
  suspend fun getCommittedEntities(): List<CommittedEntity>
  suspend fun registerCommittedEntities(committed: List<CommittedEntity>)
}