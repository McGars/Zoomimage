package mcgars.com.zoomimage.listeners;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alexvasilkov.gestures.animation.ViewPositionAnimator;
import com.alexvasilkov.gestures.transition.ViewsCoordinator;
import com.alexvasilkov.gestures.transition.ViewsTracker;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;

public class FromViewPagerListener<ID> implements ViewsCoordinator.OnRequestViewListener<ID> {

    private final ViewPager mViewPager;
    private final ViewsTracker<ID> mTracker;
    private final ViewsTransitionAnimator<ID> mAnimator;

    private ID mId;

    public FromViewPagerListener(@NonNull ViewPager listView,
                                 @NonNull ViewsTracker<ID> tracker,
                                 @NonNull ViewsTransitionAnimator<ID> animator) {
        mViewPager = listView;
        mTracker = tracker;
        mAnimator = animator;

        mAnimator.addPositionUpdateListener(new UpdateListener());
    }

    @Override
    public void onRequestView(@NonNull ID id) {
        // Trying to find requested view on screen. If it is not currently on screen
        // or it is not fully visible than we should scroll to it at first.
        mId = id;
        int position = mTracker.getPositionForId(id);

        if (position == ViewsTracker.NO_POSITION) {
            return; // Nothing we can do
        }

        mViewPager.setCurrentItem(position, false); //Where "position" is the position you want to go

        View view = mTracker.getViewForPosition(position);
        mAnimator.setFromView(mId, view);
    }

    private class UpdateListener implements ViewPositionAnimator.PositionUpdateListener {
        @Override
        public void onPositionUpdate(float state, boolean isLeaving) {
            if (state == 0f && isLeaving) {
                mId = null;
            }
            mViewPager.setVisibility(state == 1f && !isLeaving ? View.INVISIBLE : View.VISIBLE);
        }
    }

}
