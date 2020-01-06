package work.kcs_labo.dara_log.domain.interactor

import work.kcs_labo.dara_log.data.AppRepository
import work.kcs_labo.dara_log.domain.entity.CommittedTaskEntity
import work.kcs_labo.dara_log.domain.usecase.GetCommittedTasksUseCase
import java.text.SimpleDateFormat
import java.util.*

class GetCommittedTasksInteractor(private val repository: AppRepository) :
  GetCommittedTasksUseCase {
  override suspend fun getCommittedTasks(date: Calendar): List<CommittedTaskEntity> {
    val committedDTOs = repository.getCommittedTasks(date)
    return committedDTOs.map { dto ->
      val sdfSource = SimpleDateFormat("yyyyMMdd", Locale.JAPAN)
      val parseDate = sdfSource.parse(dto.yyyymmdd)
      val calendar = Calendar.getInstance()
        .also { it.time = parseDate }

      CommittedTaskEntity(
        date = calendar,
        text = dto.text,
        shortText = dto.shortText,
        imageId = dto.imageId
      )
    }
  }
}