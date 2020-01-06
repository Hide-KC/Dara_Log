package work.kcs_labo.dara_log.domain.usecase

import work.kcs_labo.dara_log.domain.entity.CheckBoxEntity

interface LoadCheckBoxEntitiesUseCase {
  suspend fun loadCheckBoxEntities(): List<CheckBoxEntity>
}