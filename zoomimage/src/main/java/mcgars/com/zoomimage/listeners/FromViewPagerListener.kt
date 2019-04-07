package mcgars.com.zoomimage.listeners

import android.view.View
import androidx.viewpager.widget.ViewPager
import com.alexvasilkov.gestures.animation.ViewPositionAnimator
import com.alexvasilkov.gestures.transition.ViewsCoordinator
import com.alexvasilkov.gestures.transition.ViewsTracker
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator

class FromViewPagerListener<ID>(
        private val mViewPager: ViewPager,
        private val mTracker: ViewsTracker<ID>,
        private val mAnimator: ViewsTransitionAnimator<ID>
) : ViewsCoordinator.OnRequestViewListener<ID> {

    init {
        mAnimator.addPositionUpdateListener(UpdateListener())
    }

    override fun onRequestView(id: ID) {
        // Trying to find requested view on screen. If it is not currently on screen
        // or it is not fully visible than we should scroll to it at first.
        val position = mTracker.getPositionForId(id)

        if (position == ViewsTracker.NO_POSITION) {
            return  // Nothing we can do
        }

        mViewPager.setCurrentItem(position, false) //Where "position" is the position you want to go

        val view = mTracker.getViewForPosition(position)
        mAnimator.setFromView(id, view)
    }

    private inner class UpdateListener : ViewPositionAnimator.PositionUpdateListener {
        override fun onPositionUpdate(state: Float, isLeaving: Boolean) {
            mViewPager.visibility = if (state == 1f && !isLeaving) View.INVISIBLE else View.VISIBLE
        }
    }

}
