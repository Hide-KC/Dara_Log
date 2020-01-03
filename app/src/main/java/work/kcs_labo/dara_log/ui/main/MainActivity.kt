package work.kcs_labo.dara_log.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import work.kcs_labo.dara_log.R
import work.kcs_labo.dara_log.databinding.MainActivityBinding
import work.kcs_labo.dara_log.util.obtainViewModel

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = DataBindingUtil.setContentView<MainActivityBinding>(
      this, R.layout.main_activity
    ).also {
      it.viewModel = obtainViewModel()
      it.lifecycleOwner = this
    }


  }

  fun obtainViewModel() = obtainViewModel(MainActivityViewModel::class.java)
}
