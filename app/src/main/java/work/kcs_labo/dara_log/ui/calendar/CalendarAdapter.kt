package work.kcs_labo.dara_log.ui.calendar

import android.animation.Animator
import android.animation.AnimatorInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import work.kcs_labo.dara_log.R
import work.kcs_labo.dara_log.databinding.CalendarHeaderBinding
import work.kcs_labo.dara_log.databinding.CalendarItemBinding
import work.kcs_labo.pinninglistview.PinningListDBListener
import java.util.*

class CalendarAdapter(
  val viewModel: CalendarActivityViewModel,
  private val parentLifecycleOwner: LifecycleOwner
) :
  RecyclerView.Adapter<CalendarAdapter.ContentViewHolder>(),
  PinningListDBListener<Content.CalendarItem, Calendar, Content.CalendarHeader> {

  override val headerLayout: Int?
    get() = getLayoutRes(VIEW_TYPE_HEADER)

  override fun bindHeaderData(binding: ViewDataBinding, position: Int) {
    val content = viewModel.getCalendarContent(position) as Content.CalendarHeader
    (binding as CalendarHeaderBinding).header = content
  }

  override fun getCurrentHeaderPosition(position: Int): Int? {
    var index = position
    while (index > -1) {
      if (isHeader(index)) return index
      index--
    }
    return null
  }

  override fun isHeader(position: Int): Boolean =
    viewModel.getCalendarContent(position) is Content.CalendarHeader

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder =
    ContentViewHolder(
      LayoutInflater.from(parent.context).inflate(
        getLayoutRes(viewType),
        parent,
        false
      )
    )

  override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
    when (val content = viewModel.getCalendarContent(position)) {
      is Content.CalendarHeader -> {
        (holder.binding as CalendarHeaderBinding)
          .also {
            it.header = content
            it.lifecycleOwner = parentLifecycleOwner
            it.root.setOnClickListener {
              viewModel.onCalendarHeaderClicked.call(content)
            }
          }
      }
      is Content.CalendarItem -> {
        (holder.binding as CalendarItemBinding)
          .also { binding ->
            binding.item = content
            binding.lifecycleOwner = parentLifecycleOwner

            val resId = if (content._committedEntities.isNotEmpty()) {
              content._committedEntities[0].imageId
            } else {
              R.drawable.ic_yokoninattekanngaeru_hito_zzz
            }

            binding.pictogram.setImageResource(resId)

            binding.root.setOnClickListener {
              viewModel.onCalendarItemClicked.call(content)
            }
          }
      }
    }
    holder.binding.executePendingBindings()
  }

  override fun getItemViewType(position: Int): Int {
    return when (viewModel.getCalendarContent(position)) {
      is Content.CalendarHeader -> VIEW_TYPE_HEADER
      is Content.CalendarItem -> VIEW_TYPE_ITEM
    }
  }

  override fun getItemCount(): Int = viewModel.getCalendarContentsCount()

  private fun getLayoutRes(viewType: Int) =
    when (viewType) {
      VIEW_TYPE_HEADER -> R.layout.calendar_header
      VIEW_TYPE_ITEM -> R.layout.calendar_item
      else -> throw IllegalArgumentException("Unknown viewType $viewType")
    }

  companion object {
    const val VIEW_TYPE_HEADER = 1
    const val VIEW_TYPE_ITEM = 2
  }

  class ContentViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val binding: ViewDataBinding = DataBindingUtil.bind(v)!!
  }
}