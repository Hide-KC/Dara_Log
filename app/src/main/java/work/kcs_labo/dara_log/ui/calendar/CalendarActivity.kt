package work.kcs_labo.dara_log.ui.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import work.kcs_labo.dara_log.R
import work.kcs_labo.dara_log.databinding.CalendarActivityBinding

class CalendarActivity : AppCompatActivity() {
  private lateinit var viewModel: CalendarActivityViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = obtainViewModel()

    val binding = DataBindingUtil.setContentView<CalendarActivityBinding>(
      this, R.layout.calendar_activity
    ).also {
      it.viewModel = viewModel
      it.lifecycleOwner = this
    }
  }

  fun obtainViewModel() = ViewModelProviders.of(this).get(CalendarActivityViewModel::class.java)
}
