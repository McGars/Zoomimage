package mcgars.com.zoomimage.ui;

import android.widget.ImageView;

/**
 * Created by Владимир on 13.05.2016.
 */
public interface ZoomHolder {
    /**
     * For {@link android.support.v7.widget.RecyclerView.ViewHolder}  just return image
     * ignoring position
     * For adapters return ImageView
     * @param position current position of image
     * @return
     */
    ImageView getImage(int position);
}
