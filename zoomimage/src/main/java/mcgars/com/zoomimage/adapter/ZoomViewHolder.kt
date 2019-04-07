package mcgars.com.zoomimage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter
import com.alexvasilkov.gestures.views.GestureImageView
import mcgars.com.zoomimage.R

class ZoomViewHolder(
        parent: ViewGroup
) : RecyclePagerAdapter.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.zoom_item_image_full, parent, false)
) {
    val image: GestureImageView = itemView.findViewById<GestureImageView>(R.id.flickr_full_image).apply {
        controller.settings.setOverscrollDistance(0f, 0f)
    }
}