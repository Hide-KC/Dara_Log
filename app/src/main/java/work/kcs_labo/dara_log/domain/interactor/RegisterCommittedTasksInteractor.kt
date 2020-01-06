package work.kcs_labo.dara_log.domain.interactor

import work.kcs_labo.dara_log.data.AppRepository
import work.kcs_labo.dara_log.data.CommittedDTO
import work.kcs_labo.dara_log.domain.entity.CommittedTaskEntity
import work.kcs_labo.dara_log.domain.usecase.RegisterCommittedTasksUseCase
import java.text.SimpleDateFormat
import java.util.*

class RegisterCommittedTasksInteractor(private val repository: AppRepository) :
  RegisterCommittedTasksUseCase {
  override suspend fun registerCommittedTasks(committed: List<CommittedTaskEntity>) {
    if (committed.isEmpty()) return

    val registered = repository.getCommittedTasks(committed[0].date)
    val committedDTOs = committed.map { entity ->
      val sdfSource = SimpleDateFormat("yyyyMMdd", Locale.JAPAN)
      val parseDate = sdfSource.format(committed[0].date.time)

      val sameDTO = if (!registered.isNullOrEmpty()) {
        registered.find { dto -> dto.text == entity.text }
      } else {
        null
      }

      CommittedDTO(
        _id = sameDTO?._id ?: 0,
        yyyymmdd = parseDate,
        text = entity.text,
        shortText = entity.shortText,
        imageId = entity.imageId
      )
    }

    repository.deleteCommittedTasks(committed[0].date)
    repository.registerCommittedTasks(committedDTOs)
  }
}