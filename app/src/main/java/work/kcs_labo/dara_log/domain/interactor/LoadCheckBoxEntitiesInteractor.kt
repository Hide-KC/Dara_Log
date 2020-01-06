package work.kcs_labo.dara_log.domain.interactor

import work.kcs_labo.dara_log.data.AppRepository
import work.kcs_labo.dara_log.domain.entity.CheckBoxEntity
import work.kcs_labo.dara_log.domain.usecase.LoadCheckBoxEntitiesUseCase

class LoadCheckBoxEntitiesInteractor(private val repository: AppRepository) :
  LoadCheckBoxEntitiesUseCase {
  override suspend fun loadCheckBoxEntities(): List<CheckBoxEntity> {
    val checkBoxDTOs = repository.loadCheckBoxDTOs()
    return checkBoxDTOs.map { dto ->
      CheckBoxEntity(
        id = dto.checkboxId,
        text = dto.text,
        shortText = dto.shortText,
        imageId = dto.imageId,
        isChecked = false
      )
    }
  }
}