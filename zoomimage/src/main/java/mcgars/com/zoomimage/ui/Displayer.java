package mcgars.com.zoomimage.ui;

import android.widget.ImageView;

import mcgars.com.zoomimage.adapter.ZoomPhotoPagerAdapter;

/**
 * Created by Владимир on 18.10.2016.
 */

public interface Displayer {
    void displayImage(ZoomPhotoPagerAdapter.IPhoto photo, ZoomPhotoPagerAdapter.ViewHolder v);
    void cancel(ImageView v);
}
