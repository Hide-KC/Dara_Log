package work.kcs_labo.dara_log.ui.main;

import android.content.Intent
import android.os.Bundle
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

    val entities = listOf(
      CheckBoxEntity("ご飯を作った", shortText = "ご飯", imageId = R.drawable.ic_ryourisuru_hito),
      CheckBoxEntity("ゴミ出しした", shortText = "ゴミ", imageId = R.drawable.ic_gomidasisuru_hito),
      CheckBoxEntity("勉強した", shortText = "勉強", imageId = R.drawable.ic_benkyousuru_hito),
      CheckBoxEntity("掃除をした", shortText = "掃除", imageId = R.drawable.ic_soujisuru_hito),
      CheckBoxEntity("創作した", shortText = "創作", imageId = R.drawable.ic_sousakusuru_hito),
      CheckBoxEntity("送迎した", shortText = "送迎", imageId = R.drawable.ic_oyako),
      CheckBoxEntity("予備１", shortText = "何か", imageId = R.drawable.ic_gattu_pause),
      CheckBoxEntity("予備２", shortText = "鷹のポーズ", imageId = R.drawable.ic_yahooosuru_hito)
    )
    viewModel.setCheckBoxEntities(entities)

    val adapter = CheckBoxListAdapter(viewModel, this)
    binding.recyclerView.adapter = adapter
    binding.recyclerView.layoutManager = GridLayoutManager(activity, 2)

    viewModel.onClickMakeLogBtnEvent.observe(this, Observer {
      val entities = viewModel.checkBoxEntitiesLiveData.value
      if (entities != null) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, TweetContentsBuilder.create(entities))
        activity?.startActivity(Intent.createChooser(intent, "hoge"))
      }
    })

    viewModel.onClickTweetLogBtnEvent.observe(this, Observer {
      (activity as MainActivity)
    })

    binding.tweetLogBtn.setOnClickListener {
      viewModel.onClickMakeLogBtn()
    }

    binding.makeLogBtn.setOnClickListener {
      viewModel.onClickTweetLogBtn()
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