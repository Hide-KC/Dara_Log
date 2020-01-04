package work.kcs_labo.dara_log.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import work.kcs_labo.dara_log.domain.entity.CheckBoxEntity

class MainActivityViewModel(private val app: Application) : AndroidViewModel(app) {
  private val _checkBoxEntitiesLiveData = MutableLiveData<List<CheckBoxEntity>>()
  val checkBoxEntitiesLiveData: LiveData<List<CheckBoxEntity>>
    get() = _checkBoxEntitiesLiveData

  fun setCheckBoxEntities(entities: List<CheckBoxEntity>) {
    _checkBoxEntitiesLiveData.value = entities
  }
}