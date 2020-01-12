package work.kcs_labo.dara_log.domain.interactor

import work.kcs_labo.dara_log.data.AppRepository
import work.kcs_labo.dara_log.data.CommittedDTO
import work.kcs_labo.dara_log.domain.entity.CommittedEntity
import work.kcs_labo.dara_log.domain.usecase.CommittedEntityUseCase
import java.text.SimpleDateFormat
import java.util.*

class CommittedEntityInteractor(private val repository: AppRepository) :
  CommittedEntityUseCase {
  override suspend fun getCommittedEntities(date: Calendar): List<CommittedEntity> =
    mapToEntities(repository.getCommittedDTOs(date))

  override suspend fun getCommittedEntities(): List<CommittedEntity> =
    mapToEntities(repository.getCommittedDTOs())

  override suspend fun registerCommittedEntities(committed: List<CommittedEntity>) {
    if (committed.isEmpty()) return

    val registered = repository.getCommittedDTOs(committed[0].date)
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

    repository.deleteCommittedDTOs(committed[0].date)
    repository.registerCommittedDTOs(committedDTOs)
  }

  private fun mapToEntities(committedDTOs: List<CommittedDTO>): List<CommittedEntity> =
    committedDTOs.map { dto ->
      val sdfSource = SimpleDateFormat("yyyyMMdd", Locale.JAPAN)
      val parseDate = sdfSource.parse(dto.yyyymmdd)
      val calendar = Calendar.getInstance()
        .also { it.time = parseDate }

      CommittedEntity(
        date = calendar,
        text = dto.text,
        shortText = dto.shortText,
        imageId = dto.imageId
      )
    }
}