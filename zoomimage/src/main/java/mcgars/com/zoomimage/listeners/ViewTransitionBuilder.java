package mcgars.com.zoomimage.listeners;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alexvasilkov.gestures.transition.ViewsTracker;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;
import com.alexvasilkov.gestures.transition.internal.FromListViewListener;
import com.alexvasilkov.gestures.transition.internal.FromRecyclerViewListener;
import com.alexvasilkov.gestures.transition.internal.IntoViewPagerListener;

import java.util.List;

/**
 * Created by Феофилактов on 22.03.2016.
 */
public class ViewTransitionBuilder<ID> {
    private final ViewsTransitionAnimator<ID> animator = new ViewsTransitionAnimator<>();

    public ViewTransitionBuilder<ID> fromRecyclerView(@NonNull RecyclerView recyclerView,
                                                       @NonNull ViewsTracker<ID> tracker) {
        animator.setFromListener(new FromRecyclerViewListener<>(recyclerView, tracker, animator));
        return this;
    }

    public ViewTransitionBuilder<ID> fromListView(@NonNull ListView listView,
                                                   @NonNull ViewsTracker<ID> tracker) {
        animator.setFromListener(new FromListViewListener<>(listView, tracker, animator));
        return this;
    }

    public ViewTransitionBuilder<ID> fromViewPager(@NonNull ViewPager viewPager,
                                                    @NonNull ViewsTracker<ID> helper) {
        animator.setFromListener(new FromViewPagerListener<>(viewPager, helper, animator));
        return this;
    }

    public ViewTransitionBuilder<ID> fromImageView(@NonNull ImageView imageView,
                                                    @NonNull ViewsTracker<ID> helper) {
        animator.setFromListener(new FromImageViewListener<>(imageView, helper, animator));
        return this;
    }
    public ViewTransitionBuilder<ID> fromImageView(@NonNull List<ImageView> imageViews,
                                                   @NonNull ViewsTracker<ID> helper) {
        animator.setFromListener(new FromImagesViewListener<>(imageViews, helper, animator));
        return this;
    }

    public ViewTransitionBuilder<ID> intoViewPager(@NonNull ViewPager viewPager,
                                                    @NonNull ViewsTracker<ID> helper) {
        animator.setToListener(new IntoViewPagerListener<>(viewPager, helper, animator));
        return this;
    }

    public ViewsTransitionAnimator<ID> build() {
        return animator;
    }
}
