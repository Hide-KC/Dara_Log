package work.kcs_labo.dara_log.ui.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import work.kcs_labo.dara_log.di.Injection
import work.kcs_labo.dara_log.domain.interactor.CommittedEntityInteractor
import work.kcs_labo.dara_log.domain.usecase.CommittedEntityUseCase
import work.kcs_labo.dara_log.util.LiveEvent
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

  val onCalendarDataSetChanged = LiveEvent<Unit>()

  suspend fun loadCalendarContents() {
    viewModelScope.launch(coroutineContext) {
      val committedList = committedEntityUseCase.getCommittedEntities()

    }
  }

  private fun updateCalendarDataSet() {
    onCalendarDataSetChanged.call(Unit)
  }

  fun getCalendarContent(position: Int): Content =
    _contentsLiveData.value?.get(position)
      ?: throw IllegalStateException("ContentList is not Initialized")

  fun setCalendarContents(contents: List<Content>) {
    _contentsLiveData.value = contents
    updateCalendarDataSet()
  }

  fun getCalendarContentsCount(): Int = _contentsLiveData.value?.size ?: 0
}