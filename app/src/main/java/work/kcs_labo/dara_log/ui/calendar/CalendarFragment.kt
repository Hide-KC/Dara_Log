package work.kcs_labo.dara_log.ui.calendar

import android.animation.Animator
import android.animation.AnimatorInflater
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.*
import work.kcs_labo.dara_log.R
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
  private lateinit var animator: Animator

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

    viewModel.let {
      it.onCalendarHeaderClicked.observe(viewLifecycleOwner, "headerClicked", Observer { c ->
        println("header: ${c.date}")
      })
      it.onCalendarItemClicked.observe(viewLifecycleOwner, "itemClicked", Observer { c ->
        println("item: ${c.date}")
        println("entities: ${c._committedEntities.size}")
        animator.cancel()

        animator = when {
          c._committedEntities.size <= 1 -> AnimatorInflater.loadAnimator(
            activity,
            R.animator.anim_content_fade_in
          )
          else -> AnimatorInflater.loadAnimator(activity, R.animator.anim_content_fade_inout)
        }.also { anim -> anim.setTarget(binding.contentIcon) }

        when {
          c._committedEntities.isEmpty() -> {
            it.setDetailImage(R.drawable.ic_yokoninattekanngaeru_hito_zzz)
          }
          c._committedEntities.size == 1 -> {
            it.setDetailImage(c._committedEntities[0].imageId)
          }
          else -> {
            it.setDetailImage(c._committedEntities[0].imageId)
            animator.addListener(object : Animator.AnimatorListener {
              private var canceled = false
              private var index = 0

              override fun onAnimationRepeat(animation: Animator?) {}

              override fun onAnimationEnd(animation: Animator?) {
                if (canceled) {
                  animator.removeAllListeners()
                } else {
                  if (index < c._committedEntities.lastIndex) {
                    index++
                  } else {
                    index = 0
                  }
                  it.setDetailImage(c._committedEntities[index].imageId)
                  animation?.start()
                }
              }

              override fun onAnimationCancel(animation: Animator?) {
                canceled = true
              }

              override fun onAnimationStart(animation: Animator?) {
                canceled = false
              }
            })
          }
        }
        animator.start()
      })
    }

    // Animatorの初期化
    animator = AnimatorInflater.loadAnimator(activity, R.animator.anim_content_fade_inout)
    animator.setTarget(binding.contentIcon)

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

      it.addOnScrollListener(PagingScrollListener(layoutManager,
        reachedTop = {
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

            println("${calendar[Calendar.YEAR]} 年 ${calendar[Calendar.MONTH] + 1} 月")
            val newContents =
              CalendarContentsCreator.create(
                viewModel.committedEntities,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH]
              )

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

            println("${calendar[Calendar.YEAR]} 年 ${calendar[Calendar.MONTH] + 1} 月")
            val newContents =
              CalendarContentsCreator.create(
                viewModel.committedEntities,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH]
              )

            viewModel.insertCalendarContents(
              newContents,
              viewModel.getCalendarContentsCount()
            )
          }
        })
      )
    }

    launch(coroutineContext) {
      val now = Calendar.getInstance(Locale.JAPAN)
      val thisYear = now[Calendar.YEAR]
      val thisMonth = now[Calendar.MONTH]
      val nowAsItem = Content.CalendarHeader(now)

      val mutableContents = mutableListOf<Content>()

      for (month in thisMonth - 1..thisMonth + 1) {
        mutableContents.addAll(
          CalendarContentsCreator.create(
            viewModel.committedEntities,
            thisYear,
            month
          )
        )
      }

      val contents = mutableContents.toList()

      val nowPosition = contents
        .indexOfFirst { c ->
          c is Content.CalendarHeader && c.date == nowAsItem.date
        }

      withContext(Dispatchers.Main) {
        println("contents size in MAIN: ${contents.size}")
        viewModel.initCalendarContents(contents)

        delay(100)
        (binding.calendarContents.layoutManager as GridLayoutManager).scrollToPositionWithOffset(
          nowPosition,
          0
        )
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
    binding.calendarContents?.clearOnScrollListeners()
    animator.cancel()
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