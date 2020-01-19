package work.kcs_labo.dara_log.util

import androidx.recyclerview.widget.DiffUtil
import work.kcs_labo.dara_log.ui.calendar.Content

class RecyclerDiffCallback<T : Content>(private val old: List<T>, private val new: List<T>) :
  DiffUtil.Callback() {
  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
    old[oldItemPosition] == new[newItemPosition]

  override fun getOldListSize(): Int = old.size

  override fun getNewListSize(): Int = new.size

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val oldContent = old[oldItemPosition]
    val newContent = new[newItemPosition]

    return when {
      (oldContent is Content.CalendarHeader &&
        newContent is Content.CalendarHeader &&
        oldContent.date == newContent.date) -> true

      (oldContent is Content.CalendarItem &&
        newContent is Content.CalendarItem &&
        oldContent.date == newContent.date) -> true

      else -> false
    }
  }
}