package work.kcs_labo.dara_log.util

import android.graphics.drawable.VectorDrawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("bind:srcVector")
fun ImageView.setVectorDrawable(imageId: Int) {
  this.setImageResource(imageId)
}