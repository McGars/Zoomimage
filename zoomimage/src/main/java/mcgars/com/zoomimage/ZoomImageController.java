package mcgars.com.zoomimage;

import android.app.Activity;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexvasilkov.gestures.animation.ViewPositionAnimator;

import java.util.List;

import mcgars.com.zoomimage.adapter.ZoomPhotoPagerAdapter;
import mcgars.com.zoomimage.fabric.AMBuilder;
import mcgars.com.zoomimage.ui.Displayer;
import mcgars.com.zoomimage.utils.DecorUtils;

/**
 * Created by Владимир on 23.03.2016.
 * Зумирует картинку на передний план
 */
public class ZoomImageController implements ViewPositionAnimator.PositionUpdateListener, ViewPager.OnPageChangeListener {
    private Activity activity;
    private Displayer displayer;
    private ViewPager zoomPager;
    private ViewGroup root;
    private View back;
    private Toolbar toolbar;
    private ZoomPhotoPagerAdapter zoomAdapter;
    private View vShadow;

    public ZoomImageController(Activity activity, Displayer displayer) {
        this.activity = activity;
        this.displayer = displayer;
        init();
    }

    private void init() {

        if (zoomPager != null)
            return;

        ViewGroup decorView = (ViewGroup) activity.findViewById(android.R.id.content);
        if (decorView != null) {
            root = decorView;
            zoomPager = (ViewPager) root.findViewById(R.id.zoomFullPager);
            if (zoomPager != null) {
                initViews();
                return;
            } else {
                LayoutInflater inflater = LayoutInflater.from(activity);
                View v = inflater.inflate(R.layout.zoom_view_image, root, false);
                root.addView(v);

                zoomPager = (ViewPager) v.findViewById(R.id.zoomFullPager);
            }
            initViews();
        }

        // full image toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

        initDecorMargins();
    }

    private void initViews() {
        back = root.findViewById(R.id.flickrFullBackground);
        toolbar = (Toolbar) root.findViewById(R.id.flickrFullToolbar);
        vShadow = root.findViewById(R.id.vShadow);

        zoomPager.addOnPageChangeListener(this);
    }

    public Toolbar getZoomToolbar() {
        return toolbar;
    }

    public View getBack() {
        return back;
    }

    public ViewPager getZoomPager() {
        return zoomPager;
    }

    private void initFlickrAdapter() {
        if (zoomAdapter == null) {
            zoomAdapter = new ZoomPhotoPagerAdapter(zoomPager, displayer);
            zoomAdapter.setPositionAnimationListener(this);
        }
    }

    /**
     * Animate cosmetics views (ViewPager, Toolbar, Background)
     * @param state float
     * @param isLeaving boolean
     */
    @Override
    public void onPositionUpdate(float state, boolean isLeaving) {
        back.setVisibility(state == 0f ? View.INVISIBLE : View.VISIBLE);
        back.getBackground().setAlpha((int) (255 * state));

        vShadow.getBackground().setAlpha((int) (255 * state));
        vShadow.setVisibility(state == 0f ? View.INVISIBLE : View.VISIBLE);

        toolbar.setVisibility(state == 0f ? View.INVISIBLE : View.VISIBLE);
        if (Build.VERSION.SDK_INT > 10)
            toolbar.setAlpha(state);

        if (isLeaving && state == 0f) {
            zoomAdapter.setActivated(false);
        }
    }

    public void onDestroy() {
        if (zoomAdapter != null)
            zoomAdapter.removePositionAnimation(this);
    }

    /**
     * If image is full size closed it
     * @return if image is open true else false
     */
    public boolean onBackPressed() {
        if (zoomAdapter != null && !zoomAdapter.getAnimator().isLeaving()) {
            zoomAdapter.getAnimator().exit(true);
            return true;
        }
        return false;
    }

    /**
     * Before {@link #show(int)} need set data and builder
     *
     * @param builder set from views
     * @param data images
     * @return this
     */
    public ZoomImageController setPhotos(AMBuilder builder, List<? extends ZoomPhotoPagerAdapter.IPhoto> data) {
        initFlickrAdapter();
        zoomAdapter.from(builder);
        setPhotos(data);
        return this;
    }

    private void setPhotos(List<? extends ZoomPhotoPagerAdapter.IPhoto> data) {
        zoomPager.setAdapter(zoomAdapter);
        zoomAdapter.setPhotos(data);
    }

    /**
     * Zoom image to front
     *
     * @param position by
     */
    public void show(int position) {
        zoomAdapter.setActivated(true);
        zoomAdapter.getAnimator().enter(position, true);
    }

    protected void initDecorMargins() {
        // Adjusting margins and paddings to fit translucent decor
        if (Build.VERSION.SDK_INT >= 16) {
            if (root.getChildAt(0).getFitsSystemWindows())
                DecorUtils.marginForStatusBar(toolbar);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ZoomPhotoPagerAdapter.IPhoto item = zoomAdapter.getPhoto(position);
        if (item != null) {
            toolbar.setTitle(item.getText());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
