package work.kcs_labo.dara_log.ui.calendar

import android.app.Application
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import work.kcs_labo.dara_log.di.Injection
import work.kcs_labo.dara_log.domain.entity.CommittedEntity
import work.kcs_labo.dara_log.domain.interactor.CommittedEntityInteractor
import work.kcs_labo.dara_log.domain.usecase.CommittedEntityUseCase
import work.kcs_labo.dara_log.util.LiveEvent
import work.kcs_labo.dara_log.util.RecyclerDiffCallback
import java.util.*
import kotlin.coroutines.CoroutineContext

class CalendarActivityViewModel(
  private val app: Application,
  private val committedEntityUseCase: CommittedEntityUseCase
) : AndroidViewModel(app),
  CoroutineScope {
  constructor(app: Application) : this(
    app, CommittedEntityInteractor(
      Injection.provideTasksRepository(app.applicationContext)
    )
  )

  private val job = Job()

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.IO + job

  private val _contentsLiveData = MutableLiveData<List<Content>>(listOf())
  val contentsLiveData: LiveData<List<Content>>
    get() = _contentsLiveData

  private var _committedEntities: List<CommittedEntity> = listOf()
  val committedEntities: List<CommittedEntity>
    get() = _committedEntities

  private val _contentDetailImageLiveData = MutableLiveData<Int>()
  val contentDetailImageLiveData: LiveData<Int>
    get() = _contentDetailImageLiveData

  val onCalendarDataSetChanged = LiveEvent<DiffUtil.DiffResult>()
  val onCalendarHeaderClicked = LiveEvent<Content.CalendarHeader>()
  val onCalendarItemClicked = LiveEvent<Content.CalendarItem>()

  init {
    loadCommittedEntities()
  }

  private fun loadCommittedEntities() {
    viewModelScope.launch(coroutineContext) {
      _committedEntities = committedEntityUseCase.getCommittedEntities()
    }
  }

  /**
   * CalendarFragmentでCommittedを編集する
   */
  fun editCommittedEntities(date: Calendar) {
    /*
    * forで_contentsLiveDataを走査、
    * dateが一致するCalendarItemを削除
    * 同じポジションに新たなCalendarItemを配置
    * onCalendarDataSetChanged()
    * */
  }

  fun getCommittedEntities(date: Calendar): List<CommittedEntity> =
    (_contentsLiveData.value?.let { contents ->
      contents.find { c ->
        c is Content.CalendarItem &&
          c.rawDate[Calendar.YEAR] == date[Calendar.YEAR] &&
          c.rawDate[Calendar.MONTH] == date[Calendar.MONTH] &&
          c.rawDate[Calendar.DATE] == date[Calendar.DATE]
      } as Content.CalendarItem
    })?._committedEntities ?: listOf()

  fun setDetailImage(@DrawableRes res: Int) {
    _contentDetailImageLiveData.value = res
  }

  fun getCalendarContent(position: Int): Content =
    _contentsLiveData.value?.get(position)
      ?: throw IllegalStateException("ContentList is not Initialized")

  fun getLastCalendarHeader(): Content.CalendarHeader? {
    val content =
      _contentsLiveData.value?.let {contents ->
        contents.findLast { c -> c is Content.CalendarHeader }
      }

    return if (content != null) {
      content as Content.CalendarHeader
    } else {
      null
    }
  }

  fun getFirstCalendarHeader(): Content.CalendarHeader? {
    val content =
      _contentsLiveData.value?.let { contents ->
        contents.find { c -> c is Content.CalendarHeader }
      }

    return if (content != null) {
      content as Content.CalendarHeader
    } else {
      null
    }
  }

  @MainThread
  fun initCalendarContents(newContents: List<Content>) {
    viewModelScope.launch(coroutineContext) {
      _contentsLiveData.value?.let {
        val diffCallback = DiffUtil.calculateDiff(RecyclerDiffCallback(it, newContents))
        onCalendarDataSetChanged.post(diffCallback)
        _contentsLiveData.postValue(newContents)
      }
    }
  }

  @MainThread
  fun insertCalendarContents(contents: List<Content>, position: Int) {
    viewModelScope.launch(coroutineContext) {
      _contentsLiveData.value?.let { old ->
        val newContents = old.toMutableList()
        newContents.addAll(position, contents)
        val diffCallback = DiffUtil.calculateDiff(RecyclerDiffCallback(old, newContents))
        onCalendarDataSetChanged.post(diffCallback)
        _contentsLiveData.postValue(newContents)
      }
    }
  }

  @MainThread
  fun removeCalendarContents(range: IntRange) {
    viewModelScope.launch(coroutineContext) {
      _contentsLiveData.value?.let { old ->
        val newContents = old.toMutableList()
        for (i in range.reversed()) {
          newContents.removeAt(i)
        }
        val diffCallback = DiffUtil.calculateDiff(RecyclerDiffCallback(old, newContents))
        onCalendarDataSetChanged.post(diffCallback)
        _contentsLiveData.postValue(newContents)
      }
    }
  }

  fun getCalendarContentsCount(): Int = _contentsLiveData.value?.size ?: 0
}