package work.kcs_labo.dara_log.util

import android.graphics.drawable.Animatable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("bind:srcVector")
fun ImageView.setVectorDrawable(imageId: Int?) {
  if (imageId == null) return
  this.setImageResource(imageId)
  val d = this.drawable

  if (d is Animatable) {
    //TODO Drawableリソース切り替え時のアニメーションの実行方法
    d.start()
  }
}

@BindingAdapter("bind:updateList")
fun RecyclerView.updateContents(diffResult: DiffUtil.DiffResult?) {
  adapter?.run { diffResult?.dispatchUpdatesTo(this) }
  layoutManager?.onItemsChanged(this)
}