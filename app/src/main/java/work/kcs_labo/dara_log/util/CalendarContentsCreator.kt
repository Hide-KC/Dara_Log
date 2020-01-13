package work.kcs_labo.dara_log.util

import work.kcs_labo.dara_log.ui.calendar.Content
import java.util.*

object CalendarContentsCreator {
  fun create(year: Int, month: Int): List<Content> {
    val firstCalendar = Calendar.getInstance(Locale.JAPAN)
      .also {
        it.set(year, month, 1)
      }
    val firstDayOfWeek = firstCalendar.get(Calendar.DAY_OF_WEEK)
    val lastCalendar = firstCalendar
      .also {
        it.add(Calendar.MONTH, 1)
        it.add(Calendar.DATE, -1)
      }
    val weekCount = lastCalendar.get(Calendar.WEEK_OF_MONTH)
    val dateList = mutableListOf<Calendar>()

    for (d in 1..weekCount * 7) {
      val calendar = Calendar.getInstance(Locale.JAPAN)
        .also {
          it.set(year, month, d)
          it.add(Calendar.DATE, 1 - firstDayOfWeek)
        }
      dateList.add(calendar)
    }

    val contents = mutableListOf<Content>()

    for (i in 0 until dateList.count()) {
      if (i == 0) {
        val date = dateList
          .find { d -> d.get(Calendar.DATE) == 1 }

        if (date != null) {
          contents.add(Content.CalendarHeader(date))
        }
        contents.add(Content.CalendarItem(dateList[i]))
      } else {
        contents.add(Content.CalendarItem(dateList[i]))
      }
    }

    return contents
  }
}