package work.kcs_labo.dara_log.ui.calendar

import work.kcs_labo.dara_log.domain.entity.CommittedEntity
import java.text.SimpleDateFormat
import java.util.*

sealed class Content {
  data class CalendarHeader(
    private val _date: Calendar
  ) : Content() {
    val date: String
      get() {
        val d = _date.time
        val dateFormat = SimpleDateFormat("yyyy年M月", Locale.JAPAN)
        return dateFormat.format(d)
      }

    val rawDate = _date
  }

  data class CalendarItem(
    private val _date: Calendar,
    val _committedEntities: List<CommittedEntity>
  ) : Content() {
    val date: String
      get() {
        val d = _date.time
        val dateFormat = SimpleDateFormat("d", Locale.JAPAN)
        return dateFormat.format(d)
      }

    val rawDate = _date

  }
}