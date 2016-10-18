package mcgars.com.zoomimage.listeners;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alexvasilkov.gestures.transition.ViewsTracker;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;
import com.alexvasilkov.gestures.transition.ViewsTransitionBuilder;
import com.alexvasilkov.gestures.transition.internal.FromListViewListener;
import com.alexvasilkov.gestures.transition.internal.FromRecyclerViewListener;
import com.alexvasilkov.gestures.transition.internal.IntoViewPagerListener;

import java.util.List;

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
