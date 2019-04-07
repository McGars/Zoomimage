package mcgars.com.zoomimage.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.alexvasilkov.gestures.animation.ViewPositionAnimator
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter
import com.alexvasilkov.gestures.transition.SimpleViewsTracker
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator
import com.alexvasilkov.gestures.transition.ViewsTransitionBuilder
import com.alexvasilkov.gestures.views.GestureImageView
import mcgars.com.zoomimage.ZoomImageController
import mcgars.com.zoomimage.fabric.AMBuilder
import mcgars.com.zoomimage.model.IPhoto
import mcgars.com.zoomimage.ui.Displayer

/**
 *
 * @param viewPager this show zoomed images
 * @param displayer this load and display images
 */
class ZoomPhotoPagerAdapter(
        private val intoViewPager: ViewPager,
        private val displayer: Displayer,
        private var positionAnimationListener: ViewPositionAnimator.PositionUpdateListener? = null
) : RecyclePagerAdapter<ZoomViewHolder>() {

    companion object {
        fun getImage(holder: RecyclePagerAdapter.ViewHolder): GestureImageView {
            return (holder as ZoomViewHolder).image
        }
    }

    var animator: ViewsTransitionAnimator<Int>? = null
        private set

    private var mPhotos: List<IPhoto>? = null

    private var mActivated: Boolean = false

    fun from(builder: AMBuilder) {
        initSettingsAnimator(builder.build())
    }

    fun removePositionAnimation() {
        animator?.removePositionUpdateListener(positionAnimationListener ?: return)
    }

    fun enter(id: Int, withAnimation: Boolean) {
        animator?.enter(id, withAnimation)
    }

    fun exit(withAnimation: Boolean): Boolean {
        if (animator == null || animator?.isLeaving == true) return false
        animator?.exit(withAnimation)
        return true
    }

    fun setPhotos(photos: List<IPhoto>) {
        mPhotos = photos
        notifyDataSetChanged()
    }

    fun getPhoto(pos: Int): IPhoto? {
        return mPhotos?.getOrNull(pos)
    }

    /**
     * To prevent ViewPager from holding heavy views (with bitmaps)  while it is not showing
     * we may just pretend there are no items in this adapter ("activate" = false).
     * But once we need to run opening animation we should "activate" this adapter again.
     * Adapter is not activated by default.
     * @param activated false by default
     */
    fun setActivated(activated: Boolean) {
        if (mActivated != activated) {
            mActivated = activated
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int {
        return if (!mActivated || mPhotos == null) 0 else mPhotos!!.size
    }

    override fun onCreateViewHolder(container: ViewGroup): ZoomViewHolder {
        val holder = ZoomViewHolder(container)
        holder.image.controller.settings.setFillViewport(true).maxZoom = 4f
        holder.image.controller.enableScrollInViewPager(intoViewPager)
        return holder
    }


    override fun onBindViewHolder(holder: ZoomViewHolder, position: Int) {
        val photo = mPhotos?.getOrNull(position)
        displayer.displayImage(photo, holder)
    }


    override fun onRecycleViewHolder(holder: ZoomViewHolder) {
        super.onRecycleViewHolder(holder)
        displayer.cancel(holder.image)
        holder.image.setImageDrawable(null)
    }

    private fun initSettingsAnimator(builder: ViewsTransitionBuilder<Int>) {
        val animator = builder.intoViewPager(intoViewPager, object : SimpleViewsTracker() {
            override fun getViewForPosition(position: Int): View? {
                return ZoomPhotoPagerAdapter.getImage(getViewHolder(position) ?: return null)
            }
        }).build()


        positionAnimationListener?.let {
            animator.addPositionUpdateListener(it)
        }

        animator.setReadyListener {
            // Setting image drawable from 'from' view to 'to' to prevent flickering
            val from = animator.fromView as ImageView
            val to = animator.toView as ImageView
            if (to.drawable == null) {
                to.setImageDrawable(from.drawable)
            }
        }
        this.animator = animator
    }

}
