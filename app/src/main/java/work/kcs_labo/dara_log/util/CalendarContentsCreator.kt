package work.kcs_labo.dara_log.util

import work.kcs_labo.dara_log.ui.calendar.CalendarActivityViewModel
import work.kcs_labo.dara_log.ui.calendar.Content
import java.util.*

object CalendarContentsCreator {
  fun create(viewModel: CalendarActivityViewModel, year: Int, month: Int): List<Content> {
    val firstCalendar = Calendar.getInstance(Locale.JAPAN)
      .also {
        it.set(year, month, 1)
      }

    println("${this.javaClass.simpleName}.firstCalendar: ${firstCalendar[Calendar.YEAR]}年${firstCalendar[Calendar.MONTH]}月${firstCalendar[Calendar.DATE]}日")

    val firstDayOfWeek = firstCalendar.get(Calendar.DAY_OF_WEEK)
    val lastCalendar = firstCalendar
      .also {
        it.add(Calendar.MONTH, 1)
        it.add(Calendar.DATE, -1)
      }

    println("${this.javaClass.simpleName}.lastCalendar: ${lastCalendar[Calendar.YEAR]}年${lastCalendar[Calendar.MONTH]}月${lastCalendar[Calendar.DATE]}日")

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

    dateList.find { d ->
      d.get(Calendar.DATE) == 1
    }?.let {
      contents.add(Content.CalendarHeader(it))
    }

    for (i in 0 until dateList.count()) {
      val filteredEntities = viewModel.getCommittedEntities(dateList[i])
      if (filteredEntities.isNotEmpty()) {
        println("filteredEntities: $filteredEntities")
      }
      contents.add(Content.CalendarItem(dateList[i], filteredEntities))
    }

    return contents
  }
}