package mcgars.com.zoomimage.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alexvasilkov.gestures.animation.ViewPositionAnimator;
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter;
import com.alexvasilkov.gestures.transition.SimpleViewsTracker;
import com.alexvasilkov.gestures.transition.ViewsCoordinator;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;
import com.alexvasilkov.gestures.views.GestureImageView;

import java.util.List;

import mcgars.com.zoomimage.R;
import mcgars.com.zoomimage.ZoomImageController;
import mcgars.com.zoomimage.fabric.AMBuilder;
import mcgars.com.zoomimage.fabric.ViewTransitionBuilder;
import mcgars.com.zoomimage.ui.Displayer;

public class ZoomPhotoPagerAdapter
        extends RecyclePagerAdapter<ZoomPhotoPagerAdapter.ViewHolder> {

    private static final long PROGRESS_DELAY = 300L;
    private final ViewPager viewPager;
    private final Displayer displayer;

    private ViewPager intoViewPager;
    private ViewsTransitionAnimator<Integer> mAnimator;
//    private DisplayImageOptions params;
    private List<? extends IPhoto> mPhotos;

    private boolean mActivated;
    private ViewPositionAnimator.PositionUpdateListener positionAnimationListener;

    /**
     *
     * @param viewPager this show zoomed images
     * @param displayer this load and display images
     */
    public ZoomPhotoPagerAdapter(ViewPager viewPager, Displayer displayer) {
        this.viewPager = viewPager;
        this.displayer = displayer;
        init(viewPager);
    }

    private void initSettingsAnimator(ViewTransitionBuilder<Integer> builder) {
        mAnimator = builder.intoViewPager(intoViewPager, new SimpleViewsTracker() {
            @Override
            public View getViewForPosition(int position) {
                RecyclePagerAdapter.ViewHolder holder = getViewHolder(
                        position);
                return holder == null ? null : ZoomPhotoPagerAdapter.getImage(holder);
            }
        })
        .build();


        mAnimator.addPositionUpdateListener(positionAnimationListener);
        mAnimator.setReadyListener(new ViewsCoordinator.OnViewsReadyListener<Integer>() {
            @Override
            public void onViewsReady(@NonNull Integer id) {
                // Setting image drawable from 'from' view to 'to' to prevent flickering
                ImageView from = (ImageView) mAnimator.getFromView();
                ImageView to = (ImageView) mAnimator.getToView();
                if (to.getDrawable() == null) {
                    to.setImageDrawable(from.getDrawable());
                }
            }
        });
    }

    /**
     * Init in {@link ZoomImageController#init()}
     * @param intoViewPager for show full size images
     */
    private void init(ViewPager intoViewPager) {
        this.intoViewPager = intoViewPager;
    }

    public void setPositionAnimationListener(ViewPositionAnimator.PositionUpdateListener positionAnimationListener){
        this.positionAnimationListener = positionAnimationListener;
    }

    public void removePositionAnimation(ViewPositionAnimator.PositionUpdateListener positionAnimationListener){
        if(mAnimator!=null)
            mAnimator.removePositionUpdateListener(positionAnimationListener);
    }

    public ViewsTransitionAnimator<Integer> getAnimator() {
        return mAnimator;
    }

    public void setPhotos(List<? extends IPhoto> photos) {
        mPhotos = photos;
        notifyDataSetChanged();
    }

    public IPhoto getPhoto(int pos) {
        return mPhotos == null || pos < 0 || pos >= mPhotos.size() ? null : mPhotos.get(pos);
    }

    /**
     * To prevent ViewPager from holding heavy views (with bitmaps)  while it is not showing
     * we may just pretend there are no items in this adapter ("activate" = false).
     * But once we need to run opening animation we should "activate" this adapter again.
     * Adapter is not activated by default.
     * @param activated false by default
     */
    public void setActivated(boolean activated) {
        if (mActivated != activated) {
            mActivated = activated;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return !mActivated || mPhotos == null ? 0 : mPhotos.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup container) {
        final ViewHolder holder = new ViewHolder(container);
        holder.image.getController().getSettings().setFillViewport(true).setMaxZoom(3f);
        holder.image.getController().enableScrollInViewPager(intoViewPager);
//        holder.image.getController().setOnGesturesListener(gestureListener);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // Temporary disabling touch controls
        if (!holder.gesturesDisabled) {
            holder.image.getController().getSettings().disableGestures();
            holder.gesturesDisabled = true;
        }

        holder.progress.setVisibility(View.VISIBLE);

        IPhoto photo = mPhotos.get(position);

        displayer.displayImage(photo, holder);
    }



    @Override
    public void onRecycleViewHolder(@NonNull ViewHolder holder) {
        super.onRecycleViewHolder(holder);

        if (holder.gesturesDisabled) {
            holder.image.getController().getSettings().enableGestures();
            holder.gesturesDisabled = false;
        }

        displayer.cancel(holder.image);
//        ImageLoader.getInstance().cancelDisplayTask(holder.image);
        holder.progress.setVisibility(View.GONE);
        holder.image.setImageDrawable(null);
    }

    public static GestureImageView getImage(RecyclePagerAdapter.ViewHolder holder) {
        return ((ViewHolder) holder).image;
    }

    public void from(AMBuilder builder) {
        initSettingsAnimator(builder.getBuilder());
    }

    public static class ViewHolder extends RecyclePagerAdapter.ViewHolder {
        public final GestureImageView image;
        public final View progress;

        public boolean gesturesDisabled;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.zoom_item_image_full, null));
            image = (GestureImageView) itemView.findViewById(R.id.flickr_full_image);
            image.getController().getSettings()
                 .setOverscrollDistance(0, 0);
            progress = itemView.findViewById(R.id.flickr_full_progress);
        }

        public void loadComplete() {
            progress.setVisibility(View.GONE);
            if (gesturesDisabled) {
                image.getController().getSettings().enableGestures();
                gesturesDisabled = false;
            }
        }
    }

    public static class Photo implements IPhoto {
        String original, preview;

        public Photo(String original, String preview) {
            this.original = original;
            this.preview = preview;
        }

        @Override
        public String getPreview() {
            return preview;
        }

        @Override
        public String getOriginal() {
            return original;
        }

        @Override
        public String getText() {
            return null;
        }
    }

    public interface IPhoto {
        String getPreview();
        String getOriginal();
        String getText();
    }

}
