package work.kcs_labo.dara_log.ui.calendar;

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import work.kcs_labo.dara_log.databinding.CalendarFragmentBinding
import work.kcs_labo.dara_log.util.CalendarContentsCreator
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by hide1 on 2020/01/04.
 * ProjectName Dara_Log
 */

class CalendarFragment : Fragment(), CoroutineScope {
  private var job: Job = Job()
  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Default

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
    binding.calendarContents.adapter = adapter
    binding.calendarContents.layoutManager =
      CalendarLayoutManager(
        context!!,
        7,
        adapter
      )



    launch(coroutineContext) {
      val now = Calendar.getInstance(Locale.JAPAN)
      val thisYear = now[Calendar.YEAR]
      val thisMonth = now[Calendar.MONTH]
      val nowAsItem = Content.CalendarHeader(now)

      val mutableContents = mutableListOf<Content>()

      for (month in thisMonth - 6..thisMonth + 6) {
        mutableContents.addAll(CalendarContentsCreator.create(thisYear, month))
      }

      val contents = mutableContents.toList()

      val nowPosition = contents
        .indexOfFirst { c ->
          c is Content.CalendarHeader && c.date == nowAsItem.date
        }

      withContext(Dispatchers.Main) {
        println("contents size in MAIN: ${contents.size}")
        viewModel.setCalendarContents(contents)
        binding.calendarContents.scrollToPosition(nowPosition)
      }
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
    this.job.cancel()
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