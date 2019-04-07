package mcgars.com.zoomimage.listeners

import android.view.View
import android.widget.ImageView

import com.alexvasilkov.gestures.animation.ViewPositionAnimator
import com.alexvasilkov.gestures.transition.ViewsCoordinator
import com.alexvasilkov.gestures.transition.ViewsTracker
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator

class FromImagesViewListener<ID>(
        private val images: List<ImageView>,
        private val mTracker: ViewsTracker<ID>,
        private val mAnimator: ViewsTransitionAnimator<ID>
) : ViewsCoordinator.OnRequestViewListener<ID> {

    private var mId: ID? = null

    init {
        mAnimator.addPositionUpdateListener(UpdateListener())
    }

    override fun onRequestView(id: ID) {
        // Trying to find requested view on screen. If it is not currently on screen
        // or it is not fully visible than we should scroll to it at first.
        mId = id
        val position = mTracker.getPositionForId(id)
        if (position == ViewsTracker.NO_POSITION) {
            return  // Nothing we can do
        }

        val imageView = images[position]
        mAnimator.setFromView(id, imageView)
    }

    private inner class UpdateListener : ViewPositionAnimator.PositionUpdateListener {
        override fun onPositionUpdate(state: Float, isLeaving: Boolean) {
            val position = mTracker.getPositionForId(mId ?: return)
            if (state == 0f && isLeaving) {
                mId = null
            }
            val imageView = images[position]
            imageView.visibility = if (state == 0f && isLeaving) View.VISIBLE else View.INVISIBLE
        }
    }

}
