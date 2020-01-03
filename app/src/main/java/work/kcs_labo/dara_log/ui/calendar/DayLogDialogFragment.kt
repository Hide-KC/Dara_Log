package work.kcs_labo.dara_log.ui.calendar;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import work.kcs_labo.dara_log.R
import work.kcs_labo.dara_log.databinding.DayLogDialogFragmentBinding

/**
 * Created by hide1 on 2020/01/04.
 * ProjectName Dara_Log
 */

class DayLogDialogFragment : DialogFragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val viewModel = (activity as CalendarActivity).obtainViewModel()

    val binding = DataBindingUtil.inflate<DayLogDialogFragmentBinding>(
      inflater, R.layout.day_log_dialog_fragment, container, false
    ).also {
      it.viewModel = viewModel
      it.lifecycleOwner = this
    }



    return binding.root
  }

  companion object {
    fun getInstance(bundle: Bundle?): DayLogDialogFragment {
      val fragment = DayLogDialogFragment()
      fragment.arguments = bundle
      return fragment
    }
  }
}