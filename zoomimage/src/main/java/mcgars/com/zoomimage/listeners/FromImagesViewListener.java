package mcgars.com.zoomimage.listeners;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.alexvasilkov.gestures.animation.ViewPositionAnimator;
import com.alexvasilkov.gestures.transition.ViewsCoordinator;
import com.alexvasilkov.gestures.transition.ViewsTracker;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;

import java.util.List;

public class FromImagesViewListener<ID> implements ViewsCoordinator.OnRequestViewListener<ID> {


    private final List<ImageView> images;
    private final ViewsTracker<ID> mTracker;
    private final ViewsTransitionAnimator<ID> mAnimator;

    private ID mId;

    public FromImagesViewListener(@NonNull List<ImageView> imageView,
                                  @NonNull ViewsTracker<ID> tracker,
                                  @NonNull ViewsTransitionAnimator<ID> animator) {
        this.images = imageView;
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

        ImageView imageView = images.get(position);
        mAnimator.setFromView(mId, imageView);
    }

    private class UpdateListener implements ViewPositionAnimator.PositionUpdateListener {
        @Override
        public void onPositionUpdate(float state, boolean isLeaving) {
            int position = mTracker.getPositionForId(mId);
            if (state == 0f && isLeaving) {
                mId = null;
            }
            ImageView imageView = images.get(position);
            imageView.setVisibility(state == 0f && isLeaving ? View.VISIBLE : View.INVISIBLE);
        }
    }

}
