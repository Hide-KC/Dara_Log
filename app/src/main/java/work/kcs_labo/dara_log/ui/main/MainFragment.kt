package work.kcs_labo.dara_log.ui.main;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import work.kcs_labo.dara_log.databinding.MainFragmentBinding

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

    return binding.root
  }

  companion object {
    fun getInstance(bundle: Bundle?): MainFragment {
      val fragment = MainFragment()
      fragment.arguments = bundle
      return fragment
    }
  }
}