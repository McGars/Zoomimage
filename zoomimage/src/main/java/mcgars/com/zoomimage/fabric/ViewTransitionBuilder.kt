package mcgars.com.zoomimage.fabric

import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.alexvasilkov.gestures.transition.ViewsTracker
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator
import com.alexvasilkov.gestures.transition.ViewsTransitionBuilder
import mcgars.com.zoomimage.listeners.FromImageViewListener
import mcgars.com.zoomimage.listeners.FromImagesViewListener
import mcgars.com.zoomimage.listeners.FromViewPagerListener

class ViewTransitionBuilder<ID> : ViewsTransitionBuilder<ID>() {

    fun fromViewPager(
            viewPager: ViewPager,
            helper: ViewsTracker<ID>
    ): ViewTransitionBuilder<ID> {
        build().setFromListener(FromViewPagerListener(viewPager, helper, build()))
        return this
    }

    fun fromImageView(
            imageView: ImageView,
            helper: ViewsTracker<ID>
    ): ViewTransitionBuilder<ID> {
        build().setFromListener(FromImageViewListener(imageView, helper, build()))
        return this
    }

    fun fromImageView(
            imageViews: List<ImageView>,
            helper: ViewsTracker<ID>
    ): ViewTransitionBuilder<ID> {
        build().setFromListener(FromImagesViewListener(imageViews, helper, build()))
        return this
    }

}
