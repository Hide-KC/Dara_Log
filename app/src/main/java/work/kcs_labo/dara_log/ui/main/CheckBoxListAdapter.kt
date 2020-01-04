package work.kcs_labo.dara_log.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import work.kcs_labo.dara_log.databinding.CustomCheckboxBinding
import work.kcs_labo.dara_log.domain.entity.CheckBoxEntity

class CheckBoxListAdapter(
  private val viewModel: MainActivityViewModel,
  private val parentLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<CheckBoxListAdapter.CheckBoxViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckBoxViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding = CustomCheckboxBinding.inflate(inflater, parent, false)
    return CheckBoxViewHolder(binding)
  }

  override fun getItemCount(): Int {
    val list = viewModel.checkBoxEntitiesLiveData.value
    return list?.size ?: 0
  }

  override fun onBindViewHolder(holder: CheckBoxViewHolder, position: Int) {
    val entities = viewModel.checkBoxEntitiesLiveData.value
    holder.binding.lifecycleOwner = parentLifecycleOwner
    if (entities != null) {
      holder.binding.checkbox.text = entities[position].text
      holder.binding.checkbox.isChecked = entities[position].isChecked
    }
  }

  class CheckBoxViewHolder(val binding: CustomCheckboxBinding) : RecyclerView.ViewHolder(binding.root)
}