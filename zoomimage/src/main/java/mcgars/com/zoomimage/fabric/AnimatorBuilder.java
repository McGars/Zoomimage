package mcgars.com.zoomimage.fabric;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alexvasilkov.gestures.transition.SimpleViewsTracker;

import java.util.List;

import mcgars.com.zoomimage.ui.ZoomHolder;

/**
 * Created by gars on 18.10.2016.
 */

public class AnimatorBuilder implements AMBuilder {

    ViewTransitionBuilder<Integer> builder;

    public static AnimatorBuilder getInstance() {
        return new AnimatorBuilder();
    }

    public ViewTransitionBuilder<Integer> getBuilder() {
        return builder;
    }

    public AMBuilder from(final ImageView imageView) {
        setBuilder(new ViewTransitionBuilder<Integer>()
                .fromImageView(imageView, new SimpleViewsTracker() {
                    @Override
                    public View getViewForPosition(int position) {
                        return imageView;
                    }
                }));
        return this;
    }

    public AMBuilder from(final List<ImageView> imageView) {
        setBuilder(new ViewTransitionBuilder<Integer>()
                .fromImageView(imageView, new SimpleViewsTracker() {
                    @Override
                    public View getViewForPosition(int position) {
                        return imageView.get(position);
                    }
                }));
        return this;
    }

    public AMBuilder from(final ViewPager fromViewPager) {
        setBuilder(new ViewTransitionBuilder<Integer>()
                .fromViewPager(fromViewPager, new SimpleViewsTracker() {
                    @Override
                    public View getViewForPosition(int position) {
                        return ((ZoomHolder)(fromViewPager.getAdapter())).getImage(position);
                    }
                }));
        return this;
    }

    public AMBuilder from(final RecyclerView recyclerView) {
        setBuilder((ViewTransitionBuilder<Integer>) new ViewTransitionBuilder<Integer>()
                .fromRecyclerView(recyclerView, new SimpleViewsTracker() {
                    @Override
                    public View getViewForPosition(int position) {
                        ZoomHolder holder = (ZoomHolder) recyclerView.findViewHolderForAdapterPosition(position);
                        return holder == null ? null : holder.getImage(position);
                    }
                }));
        return this;
    }

    protected void setBuilder(ViewTransitionBuilder<Integer> builder) {
        this.builder = builder;
    }
}
