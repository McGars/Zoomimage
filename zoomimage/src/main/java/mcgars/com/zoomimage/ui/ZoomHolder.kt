package mcgars.com.zoomimage.ui

import android.widget.ImageView

import androidx.recyclerview.widget.RecyclerView

interface ZoomHolder {
    /**
     * For [RecyclerView.ViewHolder]  just return image
     * ignoring position
     * For adapters return ImageView
     * @param position current position of image
     * @return ImageView will be zoomed
     */
    fun getImage(position: Int): ImageView
}
