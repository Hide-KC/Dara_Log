package work.kcs_labo.dara_log.ui.calendar

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import work.kcs_labo.dara_log.databinding.CalendarFragmentBinding
import work.kcs_labo.dara_log.util.CalendarContentsCreator
import work.kcs_labo.dara_log.util.PagingScrollListener
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by hide1 on 2020/01/04.
 * ProjectName Dara_Log
 */

class CalendarFragment : Fragment(), CoroutineScope {
  private var job: Job = Job()
  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job

  private lateinit var binding: CalendarFragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val viewModel = (activity as CalendarActivity).obtainViewModel()

    binding = CalendarFragmentBinding.inflate(inflater, container, false)
      .also {
        it.viewModel = viewModel
        it.lifecycleOwner = this
      }

    val adapter = CalendarAdapter(
      viewModel,
      this.viewLifecycleOwner
    )

    // RecyclerView Contentsの初期化
    binding.calendarContents.also {
      it.adapter = adapter

      val layoutManager = CalendarLayoutManager(
        context!!,
        7,
        adapter
      )
      it.layoutManager = layoutManager
      it.clearOnScrollListeners()

      it.addOnScrollListener(PagingScrollListener(layoutManager, reachedTop = {
        val firstHeader = viewModel.getFirstCalendarHeader()
        if (firstHeader != null) {
          val calendar = Calendar.getInstance(Locale.JAPAN).also { c ->
            c.set(
              firstHeader.rawDate[Calendar.YEAR],
              firstHeader.rawDate[Calendar.MONTH],
              firstHeader.rawDate[Calendar.DATE]
            )
            c.add(Calendar.MONTH, -1)
          }

          println("${calendar[Calendar.YEAR]} 年 ${calendar[Calendar.MONTH]} 月")
          val newContents =
            CalendarContentsCreator.create(calendar[Calendar.YEAR], calendar[Calendar.MONTH])

          viewModel.insertCalendarContents(newContents, 0)
        }
      }, reachedBottom = {
        val lastHeader = viewModel.getLastCalendarHeader()
        if (lastHeader != null) {
          val calendar = Calendar.getInstance(Locale.JAPAN).also { c ->
            c.set(
              lastHeader.rawDate[Calendar.YEAR],
              lastHeader.rawDate[Calendar.MONTH],
              lastHeader.rawDate[Calendar.DATE]
            )
            c.add(Calendar.MONTH, 1)
          }

          println("${calendar[Calendar.YEAR]} 年 ${calendar[Calendar.MONTH]} 月")
          val newContents =
            CalendarContentsCreator.create(calendar[Calendar.YEAR], calendar[Calendar.MONTH])

          viewModel.insertCalendarContents(
            newContents,
            viewModel.getCalendarContentsCount()
          )
        }
      }))
    }

    launch(coroutineContext) {
      val now = Calendar.getInstance(Locale.JAPAN)
      val thisYear = now[Calendar.YEAR]
      val thisMonth = now[Calendar.MONTH]
      val nowAsItem = Content.CalendarHeader(now)

      val mutableContents = mutableListOf<Content>()

      for (month in thisMonth - 1..thisMonth + 1) {
        mutableContents.addAll(CalendarContentsCreator.create(thisYear, month))
      }

      val contents = mutableContents.toList()

      val nowPosition = contents
        .indexOfFirst { c ->
          c is Content.CalendarHeader && c.date == nowAsItem.date
        }

      viewModel.initCalendarContents(contents)

      println("contents size in MAIN: ${contents.size}")
      (binding.calendarContents.layoutManager as GridLayoutManager).scrollToPositionWithOffset(
        nowPosition,
        0
      )
    }

    return binding.root
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    val calendarState = binding.calendarContents.layoutManager?.onSaveInstanceState()
    outState.putParcelable(CALENDAR_STATE, calendarState)
  }

  override fun onViewStateRestored(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)
    val calendarState = savedInstanceState?.getParcelable<Parcelable>(CALENDAR_STATE)
    binding.calendarContents.layoutManager?.onRestoreInstanceState(calendarState)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    println("onDestroyView")
  }

  companion object {
    private const val CALENDAR_STATE = "calendar_state"

    fun getInstance(bundle: Bundle? = null): CalendarFragment {
      val fragment = CalendarFragment()
      fragment.arguments = bundle
      return fragment
    }
  }
}