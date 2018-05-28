package maehara08.github_user_mvi.util

import android.support.v7.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent
import io.reactivex.Observable
import io.reactivex.functions.Predicate

object RxBindingHelper {
    const val SCROLL_GAP = 1

    fun scrollFilter(layoutManager: LinearLayoutManager, gap: Int = SCROLL_GAP): Predicate<RecyclerViewScrollEvent> =
            Predicate<RecyclerViewScrollEvent> {
                // 上向きのスクロールは無視する
                if (it.dy() <= 0) {
                    return@Predicate false
                }
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                return@Predicate (visibleItemCount + pastVisibleItems + gap >= totalItemCount)
            }
}

fun Observable<RecyclerViewScrollEvent>.addScrollFilter(
        layoutManager: LinearLayoutManager,
        gap: Int = RxBindingHelper.SCROLL_GAP
): Observable<RecyclerViewScrollEvent> =
        this.filter(RxBindingHelper.scrollFilter(layoutManager, gap))