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

  private val _contentDetailImageLiveData = MutableLiveData<Int>()
  val contentDetailImageLiveData: LiveData<Int>
    get() = _contentDetailImageLiveData

  private val _committedEntitiesLiveData = MutableLiveData<List<CommittedEntity>>(listOf())
  val committedEntitiesLiveData: LiveData<List<CommittedEntity>>
    get() = _committedEntitiesLiveData

  val onCalendarDataSetChanged = LiveEvent<DiffUtil.DiffResult>()
  val onCalendarHeaderClicked = LiveEvent<Content.CalendarHeader>()
  val onCalendarItemClicked = LiveEvent<Content.CalendarItem>()

  init {
    loadCommittedEntities()
  }

  private fun loadCommittedEntities() {
    viewModelScope.launch(coroutineContext) {
      _committedEntitiesLiveData.postValue(committedEntityUseCase.getCommittedEntities())
    }
  }

  fun getCommittedEntities(): List<CommittedEntity> = _committedEntitiesLiveData.value ?: listOf()

  fun getCommittedEntities(date: Calendar): List<CommittedEntity> =
    _committedEntitiesLiveData.value?.filter { entity ->
      entity.date[Calendar.YEAR] == date[Calendar.YEAR] &&
      entity.date[Calendar.MONTH] == date[Calendar.MONTH] &&
      entity.date[Calendar.DATE] == date[Calendar.DATE]
    } ?: listOf()

  fun setDetailImageLiveData(@DrawableRes res: Int) {
    _contentDetailImageLiveData.value = res
  }

  fun getCalendarContent(position: Int): Content =
    _contentsLiveData.value?.get(position)
      ?: throw IllegalStateException("ContentList is not Initialized")

  fun getLastCalendarHeader(): Content.CalendarHeader? {
    val contents = _contentsLiveData.value
    if (contents != null) {
      for (i in contents.lastIndex downTo 0 step 1) {
        val content = getCalendarContent(i)
        if (content is Content.CalendarHeader) return content
      }
      return null
    } else {
      throw IllegalStateException("ContentList is not Initialized")
    }
  }

  fun getFirstCalendarHeader(): Content.CalendarHeader? {
    val contents = _contentsLiveData.value
    if (contents != null) {
      contents.forEach { content ->
        if (content is Content.CalendarHeader) return content
      }
      return null
    } else {
      throw IllegalStateException("ContentList is not Initialized")
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