package work.kcs_labo.dara_log.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import work.kcs_labo.dara_log.ui.calendar.CalendarActivityViewModel
import work.kcs_labo.dara_log.ui.main.MainActivityViewModel

class ViewModelFactory private constructor(
  private val app: Application
) : ViewModelProvider.NewInstanceFactory() {

  override fun <T : ViewModel> create(modelClass: Class<T>) =
    with(modelClass) {
      when {
        isAssignableFrom(MainActivityViewModel::class.java) ->
          MainActivityViewModel(app)
        isAssignableFrom(CalendarActivityViewModel::class.java) ->
          CalendarActivityViewModel(app)
        else ->
          throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
      }
    } as T

  companion object {
    private var INSTANCE: ViewModelFactory? = null

    fun getInstance(app: Application): ViewModelFactory =
      INSTANCE ?: synchronized(ViewModelFactory::class.java) {
        INSTANCE ?: ViewModelFactory(app)
          .also { INSTANCE = it }
      }

    fun destroyInstance() {
      INSTANCE = null
    }
  }
}