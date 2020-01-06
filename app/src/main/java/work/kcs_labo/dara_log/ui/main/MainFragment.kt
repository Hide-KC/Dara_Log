package work.kcs_labo.dara_log.ui.main;

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.material.snackbar.Snackbar
import work.kcs_labo.dara_log.BuildConfig
import work.kcs_labo.dara_log.R
import work.kcs_labo.dara_log.databinding.MainFragmentBinding
import work.kcs_labo.dara_log.util.TweetContentsBuilder
import java.util.*

/**
 * Created by hide1 on 2020/01/04.
 * ProjectName Dara_Log
 */

@Suppress("ConstantConditionIf")
class MainFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val viewModel = (activity as MainActivity).obtainViewModel()

    val binding = MainFragmentBinding.inflate(inflater, container, false)
      .also {
        it.viewModel = viewModel
        it.lifecycleOwner = this

        val adRequest = AdRequest.Builder().build()
        it.mainAdView?.loadAd(adRequest)
      }

    viewModel.onClickTweetLogBtnEvent.observe(this, "tweet", Observer {
      val entities = viewModel.checkBoxEntitiesLiveData.value
      if (entities != null) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, TweetContentsBuilder.create(entities))
        activity?.startActivity(Intent.createChooser(intent, ""))
      }
    })

    viewModel.onClickMakeLogBtnEvent.observe(this, "makeLog", Observer {
      val entities = viewModel.checkBoxEntitiesLiveData.value
      if (!entities.isNullOrEmpty()) {
        val committed = entities
          .filter { e -> e.isChecked }
        val date = Calendar.getInstance()
          .also {
            if (BuildConfig.IS_DEBUG) {
              it.set(2020, 1, 1)
            } else {
              it.time
            }
          }
        viewModel.registerCommittedTasks(date, committed)
        val messageBar =
          Snackbar.make(binding.root, "記録しました。\nそのうちカレンダー表示も実装します。", Snackbar.LENGTH_LONG)
            .also {
              // TODO API違いによるカラーリソースの取り扱い
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.setBackgroundTint(resources.getColor(R.color.snackbarBackGround, null))
              }
            }
        messageBar.show()
      }
    })

    viewModel.checkBoxEntitiesLiveData.observe(this, Observer {
      val adapter = CheckBoxListAdapter(viewModel, this)
      binding.recyclerView.adapter = adapter
      binding.recyclerView.layoutManager = GridLayoutManager(activity, 2)
    })

    binding.tweetLogBtn.setOnClickListener {
      viewModel.onClickTweetLogBtn()
    }

    binding.makeLogBtn.setOnClickListener {
      viewModel.onClickMakeLogBtn()
    }
    return binding.root
  }

  companion object {
    fun getInstance(bundle: Bundle? = null): MainFragment {
      val fragment = MainFragment()
      fragment.arguments = bundle
      return fragment
    }
  }
}