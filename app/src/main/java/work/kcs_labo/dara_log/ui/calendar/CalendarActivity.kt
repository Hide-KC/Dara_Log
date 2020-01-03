package work.kcs_labo.dara_log.ui.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import work.kcs_labo.dara_log.R
import work.kcs_labo.dara_log.databinding.CalendarActivityBinding
import work.kcs_labo.dara_log.util.obtainViewModel

class CalendarActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = DataBindingUtil.setContentView<CalendarActivityBinding>(
      this, R.layout.calendar_activity
    ).also {
      it.viewModel = obtainViewModel()
      it.lifecycleOwner = this
    }
  }

  fun obtainViewModel() = obtainViewModel(CalendarActivityViewModel::class.java)
}
