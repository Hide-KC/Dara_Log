package work.kcs_labo.dara_log.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import work.kcs_labo.dara_log.R
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
      val entity = entities[position]
      holder.binding.checkbox.text = entity.text
      holder.binding.checkbox.isChecked = entity.isChecked

      holder.binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
        viewModel.setCheckState(entity.copy(isChecked = isChecked))
        if (isChecked) {
          when(entity.imageId) {
            -1 -> viewModel.setImageId(R.drawable.ic_gattu_pause)
            else -> viewModel.setImageId(entity.imageId)
          }
        } else {
          val checkedEntities = viewModel.getCheckedEntities()
          if (checkedEntities.isNotEmpty()) {
            viewModel.setImageId(checkedEntities[0].imageId)
          } else {
            viewModel.setImageId(R.drawable.ic_yokoninattekanngaeru_hito)
          }
        }
      }
    }
  }

  class CheckBoxViewHolder(val binding: CustomCheckboxBinding) : RecyclerView.ViewHolder(binding.root)
}