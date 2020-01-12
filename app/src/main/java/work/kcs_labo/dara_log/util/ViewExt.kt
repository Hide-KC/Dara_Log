package work.kcs_labo.dara_log.util

import android.graphics.drawable.AnimatedVectorDrawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("bind:srcVector")
fun ImageView.setVectorDrawable(imageId: Int?) {
  if (imageId == null) return
  this.setImageResource(imageId)
  val d = this.drawable

  if (d is AnimatedVectorDrawable) {
    //TODO Drawableリソース切り替え時のアニメーションの実行方法
    d.start()
  }
}

@BindingAdapter("bind:calendarUpdate")
fun RecyclerView.onCalendarDataSetChanged(unit: Unit?) {
  adapter?.notifyDataSetChanged()
}