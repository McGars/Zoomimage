package mcgars.com.zoomimage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import mcgars.com.zoomimage.ui.Displayer;

/**
 * Created by Владимир on 23.03.2016.
 */
public class ThumbAdapter extends ThumbPagerAdapter {

    private final Context context;
    private final Displayer displayer;
    private final View.OnClickListener clickListener;

    public ThumbAdapter(Context context, List<? extends ZoomPhotoPagerAdapter.IPhoto> list, Displayer displayer, View.OnClickListener clickListener) {
        this.context = context;
        this.displayer = displayer;
        this.clickListener = clickListener;
        initViews(list);
    }

    private void initViews(List<? extends ZoomPhotoPagerAdapter.IPhoto> list) {
        List<View> imagesView = new ArrayList<>(list.size());
        for (ZoomPhotoPagerAdapter.IPhoto img : list) {

            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//            ImageView v = (ImageView) inflater.inflate(R.layout.zoom_item_image, null);
//            ImageLoader.getInstance().displayImage(img.getPreview(), imageView);
            displayer.displayImage(img, imageView);
            imageView.setOnClickListener(clickListener);
            imagesView.add(imageView);
        }
        setViews(imagesView);
    }

}
