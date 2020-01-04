package work.kcs_labo.dara_log.ui.main;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import work.kcs_labo.dara_log.R
import work.kcs_labo.dara_log.databinding.MainFragmentBinding
import work.kcs_labo.dara_log.domain.entity.CheckBoxEntity
import work.kcs_labo.dara_log.util.TweetContentsBuilder

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

    val adapter = CheckBoxListAdapter(viewModel, this)
    binding.recyclerView.adapter = adapter
    binding.recyclerView.layoutManager = GridLayoutManager(activity, 2)

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
      Log.d(this.javaClass.simpleName, "Click makeLog")
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