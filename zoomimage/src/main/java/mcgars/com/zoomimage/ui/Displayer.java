package mcgars.com.zoomimage.ui;

import android.widget.ImageView;

import mcgars.com.zoomimage.adapter.ZoomPhotoPagerAdapter;

/**
 * Created by Владимир on 18.10.2016.
 */

public interface Displayer {
    /**
     * Show image with your own libs or methods
     * @param photo object
     * @param v holder from adapter who show full image
     */
    void displayImage(ZoomPhotoPagerAdapter.IPhoto photo, ZoomPhotoPagerAdapter.ViewHolder v);

    /**
     * Called when closing fool image
     * @param imageView ImageView
     */
    void cancel(ImageView imageView);
}
