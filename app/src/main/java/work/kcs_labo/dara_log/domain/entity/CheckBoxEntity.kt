package work.kcs_labo.dara_log.domain.entity

data class CheckBoxEntity(
  val text: String = "",
  val shortText: String = "",
  val imageId: Int = -1,
  val isChecked: Boolean = false
)