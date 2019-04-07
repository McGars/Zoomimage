package mcgars.com.zoomimage.ui

import android.widget.ImageView

import mcgars.com.zoomimage.adapter.ZoomPhotoPagerAdapter
import mcgars.com.zoomimage.adapter.ZoomViewHolder
import mcgars.com.zoomimage.model.IPhoto

interface Displayer {
    /**
     * Show image with your own libs or methods
     * @param photo object
     * @param v holder from adapter who show full image
     */
    fun displayImage(photo: IPhoto?, v: ZoomViewHolder)

    /**
     * Called when closing fool image
     * @param imageView ImageView
     */
    fun cancel(imageView: ImageView)
}
