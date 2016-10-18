package mcgars.com.zoomimage.listeners;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.alexvasilkov.gestures.animation.ViewPositionAnimator;
import com.alexvasilkov.gestures.transition.ViewsCoordinator;
import com.alexvasilkov.gestures.transition.ViewsTracker;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;

public class FromImageViewListener<ID> implements ViewsCoordinator.OnRequestViewListener<ID> {

    private static final Rect LOCATION_PARENT = new Rect(), LOCATION = new Rect();

    private final ImageView mViewPager;
    private final ViewsTracker<ID> mTracker;
    private final ViewsTransitionAnimator<ID> mAnimator;

    private ID mId;
    private boolean mScrollHalfVisibleItems;

    public FromImageViewListener(@NonNull ImageView listView,
                                 @NonNull ViewsTracker<ID> tracker,
                                 @NonNull ViewsTransitionAnimator<ID> animator) {
        mViewPager = listView;
        mTracker = tracker;
        mAnimator = animator;

//        mViewPager.addOnPageChangeListener(new ScrollListener());
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

//        mViewPager.setCurrentItem(position, false); //Where "2" is the position you want to go

//        mViewPager.setCurrentItem(position); //Where "2" is the position you want to go
//        View view = mTracker.getViewForPosition(position);
        mAnimator.setFromView(mId, mViewPager);


//        if (mScrollHalfVisibleItems) {
//            mViewPager.getGlobalVisibleRect(LOCATION_PARENT);
//            LOCATION_PARENT.left += mViewPager.getPaddingLeft();
//            LOCATION_PARENT.right -= mViewPager.getPaddingRight();
//            LOCATION_PARENT.top += mViewPager.getPaddingTop();
//            LOCATION_PARENT.bottom -= mViewPager.getPaddingBottom();
//
//            view.getGlobalVisibleRect(LOCATION);
//            if (!LOCATION_PARENT.contains(LOCATION)
//                    || view.getWidth() > LOCATION.width()
//                    || view.getHeight() > LOCATION.height()) {
//                mViewPager.setCurrentItem(position);
//            }
//        }
    }

//    private class ScrollListener extends ViewPager.SimpleOnPageChangeListener {
//
//        @Override
//        public void onPageSelected(int position) {
//            if (mId == null) {
//                return; // Nothing to do
//            }
//            if (mId.equals(mTracker.getIdForPosition(position))) {
//                View from = mTracker.getViewForPosition(position);
//                if (from != null) {
//                    mAnimator.setFromView(mId, from);
//                }
//            }
//        }
//    }

    private class UpdateListener implements ViewPositionAnimator.PositionUpdateListener {
        @Override
        public void onPositionUpdate(float state, boolean isLeaving) {
            if (state == 0f && isLeaving) {
                mId = null;
            }
            mViewPager.setVisibility(state == 0f && isLeaving ? View.VISIBLE : View.INVISIBLE);
            mScrollHalfVisibleItems = state == 1f; // Only scroll if we in full mode
        }
    }

}
