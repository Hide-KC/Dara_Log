package work.kcs_labo.dara_log.domain.entity

import java.util.*

data class CommittedEntity(
  val date: Calendar,
  val text: String,
  val shortText: String,
  val imageId: Int = -1
)