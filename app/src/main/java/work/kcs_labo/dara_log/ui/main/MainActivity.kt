package work.kcs_labo.dara_log.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.main_activity.*
import work.kcs_labo.dara_log.R
import work.kcs_labo.dara_log.databinding.MainActivityBinding
import work.kcs_labo.dara_log.ui.calendar.CalendarActivity

class MainActivity : AppCompatActivity() {
  private lateinit var viewModel: MainActivityViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    viewModel = obtainViewModel()

    val binding = DataBindingUtil.setContentView<MainActivityBinding>(
      this, R.layout.main_activity
    ).also {
      it.viewModel = viewModel
      it.lifecycleOwner = this
    }
    setSupportActionBar(main_toolbar)

    val fragment = MainFragment.getInstance()
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(R.id.main_contents, fragment)
    transaction.commit()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.calendar_menu -> {
        val intent = Intent(this, CalendarActivity::class.java)
        startActivity(intent)
      }
      else -> {
        throw NotImplementedError()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  fun obtainViewModel() = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
}
