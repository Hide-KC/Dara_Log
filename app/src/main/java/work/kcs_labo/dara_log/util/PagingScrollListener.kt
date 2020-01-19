package work.kcs_labo.dara_log.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PagingScrollListener(
  private val layoutManager: GridLayoutManager,
  private val reachedTop: () -> Unit,
  private val reachedBottom: () -> Unit
) : RecyclerView.OnScrollListener() {

  private var previousTotal = 0
  private var loading = true
  private var visibleThreshold = 15
  private var firstVisibleItemPosition = 0
  private var lastVisibleItemPosition = layoutManager.itemCount
  private var visibleItemCount = 0
  private var totalItemCount = 0

  override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
    super.onScrolled(rv, dx, dy)

    visibleItemCount = rv.childCount
    totalItemCount = layoutManager.itemCount
    firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
    lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

    if (loading) {
      if (totalItemCount > previousTotal) {
        loading = false
        previousTotal = totalItemCount
      }
    }

    //  最初の行からThresholdだけ下 OR 最後の行からThresholdだけ上
    if (dy < 0 && !loading && (firstVisibleItemPosition <= visibleThreshold)) {
      // Top has been reached
      reachedTop()
      loading = true
    } else if (dy > 0 && !loading && (totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold)) {
      // End has been reached
      reachedBottom()
      loading = true
    }
  }
}