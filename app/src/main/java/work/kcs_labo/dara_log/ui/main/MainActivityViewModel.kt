package work.kcs_labo.dara_log.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import work.kcs_labo.dara_log.R
import work.kcs_labo.dara_log.data.AppRepository
import work.kcs_labo.dara_log.di.Injection
import work.kcs_labo.dara_log.domain.entity.CheckBoxEntity
import work.kcs_labo.dara_log.domain.entity.CommittedTaskEntity
import work.kcs_labo.dara_log.domain.interactor.GetCommittedTasksInteractor
import work.kcs_labo.dara_log.domain.interactor.LoadCheckBoxEntitiesInteractor
import work.kcs_labo.dara_log.domain.interactor.RegisterCommittedTasksInteractor
import work.kcs_labo.dara_log.domain.usecase.GetCommittedTasksUseCase
import work.kcs_labo.dara_log.domain.usecase.LoadCheckBoxEntitiesUseCase
import work.kcs_labo.dara_log.domain.usecase.RegisterCommittedTasksUseCase
import work.kcs_labo.dara_log.util.LiveEvent
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivityViewModel(private val app: Application) : AndroidViewModel(app), CoroutineScope {
  private val getCommittedTask: GetCommittedTasksUseCase = GetCommittedTasksInteractor(
    Injection.provideTasksRepository(app.applicationContext)
  )

  private val loadCheckBoxEntities: LoadCheckBoxEntitiesUseCase = LoadCheckBoxEntitiesInteractor(
    Injection.provideTasksRepository(app.applicationContext)
  )

  private val registerCommittedTasks: RegisterCommittedTasksUseCase =
    RegisterCommittedTasksInteractor(
      Injection.provideTasksRepository(app.applicationContext)
    )

  private val job = Job()

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.IO + job

  private val _checkBoxEntitiesLiveData = MutableLiveData<List<CheckBoxEntity>>()
  val checkBoxEntitiesLiveData: LiveData<List<CheckBoxEntity>>
    get() = _checkBoxEntitiesLiveData

  private val _imageSrcLiveData = MutableLiveData<Int>(R.drawable.av_yokoninattekanngaeru)
  val imageSrcLiveData: LiveData<Int>
    get() = _imageSrcLiveData

  val onClickMakeLogBtnEvent = LiveEvent<Unit>()
  val onClickTweetLogBtnEvent = LiveEvent<Unit>()

  private var position: Int = 0

  fun onClickMakeLogBtn() {
    onClickMakeLogBtnEvent.call(Unit)
  }

  fun onClickTweetLogBtn() {
    onClickTweetLogBtnEvent.call(Unit)
  }

  fun registerCommittedTasks(date: Calendar, entities: List<CheckBoxEntity>) {
    viewModelScope.launch(coroutineContext) {
      val committed = entities
        .filter { e -> e.isChecked }
        .map { CommittedTaskEntity(date, it.text, it.shortText, it.imageId) }
      registerCommittedTasks.registerCommittedTasks(committed)
      val test = getCommittedTask.getCommittedTasks(date)
      for (t in test) {
        println("${t.date}, ${t.text}")
      }
    }
  }

  fun setCheckBoxEntities() {
    viewModelScope.launch(coroutineContext) {
      val list = async { loadCheckBoxEntities.loadCheckBoxEntities() }
      if (list.await().isNullOrEmpty()) {
        _checkBoxEntitiesLiveData.postValue(listOf())
      } else {
        _checkBoxEntitiesLiveData.postValue(list.await())
      }
    }
  }

  fun setCheckState(newEntity: CheckBoxEntity) {
    val entities = _checkBoxEntitiesLiveData.value
    if (entities != null) {
      val mutableEntities = entities.toMutableList()
      val index = mutableEntities.indexOfFirst { e -> e.text == newEntity.text }
      if (index >= 0) {
        mutableEntities.removeAt(index)
        mutableEntities.add(index, newEntity)
        _checkBoxEntitiesLiveData.value = mutableEntities.toList()
      }
    }
  }

  fun getNextCheckedEntity(): CheckBoxEntity {
    val entities = checkBoxEntitiesLiveData.value
    if (entities != null) {
      if (position + 1 <= entities.lastIndex) {
        ++position
      } else if (position + 1 > entities.lastIndex) {
        position = 0
      }
      return entities[position]
    } else {
      throw IllegalStateException()
    }
  }

  fun resetPosition() {
    position = 0
  }

  fun getCheckedEntities(): List<CheckBoxEntity> =
    checkBoxEntitiesLiveData.value?.filter { entity -> entity.isChecked } ?: listOf()

  fun setImageId(imageId: Int) {
    _imageSrcLiveData.value = imageId
  }

  override fun onCleared() {
    AppRepository.destroyInstance()
    super.onCleared()
  }
}