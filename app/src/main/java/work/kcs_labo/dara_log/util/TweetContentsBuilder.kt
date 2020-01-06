package work.kcs_labo.dara_log.util

import android.content.Context
import work.kcs_labo.dara_log.R
import work.kcs_labo.dara_log.domain.entity.CheckBoxEntity

object TweetContentsBuilder {
  fun create(context: Context, entities: List<CheckBoxEntity>): String {
    val builder = StringBuilder()
    val checkedList = entities.filter { e -> e.isChecked }
    if (checkedList.isEmpty()) {
      builder.append("だらだらしています…… ")
    } else {
      builder.append("だらけずに ")
      for (e in entities) {
        if (e.isChecked) {
          builder.append("${e.shortText} ")
        }
      }
      builder.append("を実施しました。 ")
    }

    builder.append("#だらろぐ ${context.getString(R.string.app_url)}")
    return builder.toString()
  }
}