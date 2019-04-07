package mcgars.com.zoomimage.fabric

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.alexvasilkov.gestures.transition.SimpleViewsTracker
import com.alexvasilkov.gestures.transition.ViewsTransitionBuilder
import mcgars.com.zoomimage.ui.ZoomHolder

class AnimatorBuilder private constructor(
        private val builder: ViewsTransitionBuilder<Int>
) : AMBuilder {

    companion object {

        @JvmStatic
        fun from(imageView: ImageView): AMBuilder {
            return AnimatorBuilder(ViewTransitionBuilder<Int>()
                    .fromImageView(imageView, object : SimpleViewsTracker() {
                        override fun getViewForPosition(position: Int): View {
                            return imageView
                        }
                    }))
        }

        @JvmStatic
        fun from(imageView: List<ImageView>): AMBuilder {
            return AnimatorBuilder(ViewTransitionBuilder<Int>()
                    .fromImageView(imageView, object : SimpleViewsTracker() {
                        override fun getViewForPosition(position: Int): View {
                            return imageView[position]
                        }
                    }))
        }

        @JvmStatic
        fun from(fromViewPager: ViewPager): AMBuilder {
            return AnimatorBuilder(ViewTransitionBuilder<Int>()
                    .fromViewPager(fromViewPager, object : SimpleViewsTracker() {
                        override fun getViewForPosition(position: Int): View {
                            return (fromViewPager.adapter as ZoomHolder).getImage(position)
                        }
                    }))
        }

        @JvmStatic
        fun from(recyclerView: RecyclerView): AMBuilder {
            return AnimatorBuilder(ViewTransitionBuilder<Int>()
                    .fromRecyclerView(recyclerView, object : SimpleViewsTracker() {
                        override fun getViewForPosition(position: Int): View? {
                            val holder = recyclerView.findViewHolderForAdapterPosition(position) as ZoomHolder?
                            return holder?.getImage(position)
                        }
                    }))
        }
    }

    override fun build(): ViewsTransitionBuilder<Int> {
        return builder
    }

}
