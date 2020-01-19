package work.kcs_labo.dara_log.ui.calendar

import android.app.Application
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
import work.kcs_labo.dara_log.domain.interactor.CommittedEntityInteractor
import work.kcs_labo.dara_log.domain.usecase.CommittedEntityUseCase
import work.kcs_labo.dara_log.util.LiveEvent
import work.kcs_labo.dara_log.util.RecyclerDiffCallback
import kotlin.coroutines.CoroutineContext

class CalendarActivityViewModel(private val app: Application) : AndroidViewModel(app),
  CoroutineScope {
  private val job = Job()

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.IO + job

  private val committedEntityUseCase: CommittedEntityUseCase = CommittedEntityInteractor(
    Injection.provideTasksRepository(app.applicationContext)
  )

  private val _contentsLiveData = MutableLiveData<List<Content>>(listOf())
  val contentsLiveData: LiveData<List<Content>>
    get() = _contentsLiveData

  val onCalendarDataSetChanged = LiveEvent<DiffUtil.DiffResult>()

  suspend fun loadCalendarContents() {
    viewModelScope.launch(coroutineContext) {
      val committedList = committedEntityUseCase.getCommittedEntities()

    }
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

  fun getCalendarContentsCount(): Int = _contentsLiveData.value?.size ?: 0
}