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
import work.kcs_labo.dara_log.domain.entity.CheckBoxEntity
import work.kcs_labo.dara_log.ui.calendar.CalendarActivity


class MainActivity : AppCompatActivity() {
  private lateinit var viewModel: MainActivityViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    viewModel = obtainViewModel()

    if (savedInstanceState == null) {
      viewModel.setCheckBoxEntities(listOf(
        CheckBoxEntity("ご飯を作った", shortText = "ご飯", imageId = R.drawable.ic_ryourisuru_hito),
        CheckBoxEntity("ゴミ出しした", shortText = "ゴミ出し", imageId = R.drawable.ic_gomidasisuru_hito),
        CheckBoxEntity("勉強した", shortText = "勉強", imageId = R.drawable.ic_benkyousuru_hito),
        CheckBoxEntity("掃除をした", shortText = "掃除", imageId = R.drawable.ic_soujisuru_hito),
        CheckBoxEntity("創作した", shortText = "創作活動", imageId = R.drawable.ic_sousakusuru_hito),
        CheckBoxEntity("送迎した", shortText = "送り迎え", imageId = R.drawable.ic_oyako),
        CheckBoxEntity("鷹のポーズ", shortText = "鷹のポーズ", imageId = R.drawable.ic_takanoposesuru_hito),
        CheckBoxEntity("そのほか", shortText = "そのほか", imageId = R.drawable.ic_yahooosuru_hito)
      ))
    }

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
