package work.kcs_labo.dara_log.ui.calendar

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import work.kcs_labo.dara_log.ui.calendar.CalendarAdapter.Companion.VIEW_TYPE_HEADER

class CalendarLayoutManager(context: Context, spanCount: Int, adapter: CalendarAdapter) :
  GridLayoutManager(context, spanCount) {

  init {
    this.spanSizeLookup = object : SpanSizeLookup() {
      override fun getSpanSize(position: Int): Int {
        return if (adapter.getItemViewType(position) == VIEW_TYPE_HEADER) {
          spanCount
        } else {
          1
        }
      }

      override fun getSpanIndex(position: Int, spanCount: Int): Int =
        if (adapter.getItemViewType(position) == VIEW_TYPE_HEADER) {
          0
        } else {
          val n = adapter.viewModel.contentsLiveData.value?.let {
            it.slice(0..position)
              .count { c -> c is Content.CalendarHeader }
          } ?: 0

          (position - n) % spanCount
        }
    }
  }
}