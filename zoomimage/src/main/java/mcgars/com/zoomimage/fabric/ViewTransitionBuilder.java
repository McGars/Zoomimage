package mcgars.com.zoomimage.fabric;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.alexvasilkov.gestures.transition.ViewsTracker;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;
import com.alexvasilkov.gestures.transition.ViewsTransitionBuilder;

import java.util.List;

import mcgars.com.zoomimage.listeners.FromImageViewListener;
import mcgars.com.zoomimage.listeners.FromImagesViewListener;
import mcgars.com.zoomimage.listeners.FromViewPagerListener;

/**
 * Created by Феофилактов on 22.03.2016.
 */
public class ViewTransitionBuilder<ID> extends ViewsTransitionBuilder<ID> {

    public ViewTransitionBuilder<ID> fromViewPager(@NonNull ViewPager viewPager,
                                                    @NonNull ViewsTracker<ID> helper) {
        build().setFromListener(new FromViewPagerListener<>(viewPager, helper, build()));
        return this;
    }

    public ViewTransitionBuilder<ID> fromImageView(@NonNull ImageView imageView,
                                                    @NonNull ViewsTracker<ID> helper) {
        build().setFromListener(new FromImageViewListener<>(imageView, helper, build()));
        return this;
    }
    public ViewTransitionBuilder<ID> fromImageView(@NonNull List<ImageView> imageViews,
                                                   @NonNull ViewsTracker<ID> helper) {
        build().setFromListener(new FromImagesViewListener<>(imageViews, helper, build()));
        return this;
    }

    public ViewsTransitionAnimator<ID> build() {
        return super.build();
    }
}
