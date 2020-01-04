package work.kcs_labo.dara_log.util

import work.kcs_labo.dara_log.domain.entity.CheckBoxEntity

object TweetContentsBuilder {
  fun create(entities: List<CheckBoxEntity>): String {
    val builder = StringBuilder()
    val checkedList = entities.filter { e -> e.isChecked }
    if (checkedList.isEmpty()) {
      builder.append("だらだらしています。")
    } else {
      builder.append("だらけずに ")
      for (e in entities) {
        if (e.isChecked) {
          builder.append("${e.shortText} ")
        }
      }
      builder.append("を実施しました。")
    }

    builder.append("#だらろぐ ${Constants.appUrl}")
    return builder.toString()
  }
}