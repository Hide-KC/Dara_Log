package work.kcs_labo.dara_log.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import work.kcs_labo.dara_log.R
import work.kcs_labo.dara_log.domain.entity.CheckBoxEntity
import work.kcs_labo.dara_log.util.LiveEvent
import kotlin.coroutines.CoroutineContext

class MainActivityViewModel(private val app: Application) : AndroidViewModel(app), CoroutineScope {
  private val job = Job()

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job

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

  fun setCheckBoxEntities(entities: List<CheckBoxEntity>) {
    _checkBoxEntitiesLiveData.value = entities
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
    checkBoxEntitiesLiveData.value?.filter { entity -> entity.isChecked } ?: emptyList()

  fun setImageId(imageId: Int) {
    _imageSrcLiveData.value = imageId
  }
}