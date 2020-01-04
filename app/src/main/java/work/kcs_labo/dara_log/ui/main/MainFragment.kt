package work.kcs_labo.dara_log.ui.main;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import work.kcs_labo.dara_log.databinding.MainFragmentBinding
import work.kcs_labo.dara_log.domain.entity.CheckBoxEntity

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
      CheckBoxEntity("家事をした"),
      CheckBoxEntity("ご飯を作った"),
      CheckBoxEntity("勉強した"),
      CheckBoxEntity("掃除をした"),
      CheckBoxEntity("創作した"),
      CheckBoxEntity("予備１"),
      CheckBoxEntity("予備２"),
      CheckBoxEntity("予備３")
    )
    viewModel.setCheckBoxEntities(entities)

    val adapter = CheckBoxListAdapter(viewModel, this)
    binding.recyclerView.adapter = adapter
    binding.recyclerView.layoutManager = GridLayoutManager(activity,2)
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