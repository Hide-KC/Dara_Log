package work.kcs_labo.dara_log.ui.main;

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.main_fragment.*
import work.kcs_labo.dara_log.BuildConfig
import work.kcs_labo.dara_log.databinding.MainFragmentBinding
import work.kcs_labo.dara_log.util.TweetContentsBuilder
import java.util.*

/**
 * Created by hide1 on 2020/01/04.
 * ProjectName Dara_Log
 */

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
            if(BuildConfig.IS_DEBUG) {
              it.set(2020, 1,1)
            } else {
              it.time
            }
          }
        viewModel.registerCommittedTasks(date, committed)
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