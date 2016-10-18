package mcgars.com.zoomimage;

import android.app.Activity;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alexvasilkov.gestures.animation.ViewPositionAnimator;

import java.util.List;

import mcgars.com.zoomimage.adapter.ZoomPhotoPagerAdapter;
import mcgars.com.zoomimage.ui.Displayer;
import mcgars.com.zoomimage.utils.DecorUtils;

/**
 * Created by Владимир on 23.03.2016.
 * Зумирует картинку на передний план
 */
public class ZoomImageController implements ViewPositionAnimator.PositionUpdateListener {
    private Activity activity;
    private Displayer displayer;
    private ViewPager zoomPager;
    private ViewGroup root;
    private View back;
    private Toolbar toolbar;
    private ZoomPhotoPagerAdapter flicAdapter;
    private View vShadow;

    public ZoomImageController(Activity activity, Displayer displayer){
        this.activity = activity;
        this.displayer = displayer;
        init();
    }

    private void init() {

        if(zoomPager !=null)
            return;

        ViewGroup decorView = (ViewGroup) activity.findViewById(android.R.id.content);
        if (decorView != null) {
            root = decorView;
            zoomPager = (ViewPager) root.findViewById(R.id.zoomFullPager);
            if(zoomPager !=null) {
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

    public void initFlickrAdapter(){
        if(flicAdapter == null) {
            flicAdapter = new ZoomPhotoPagerAdapter(zoomPager, displayer);
            flicAdapter.setPositionAnimationListener(this);
        }
    }

    @Override
    public void onPositionUpdate(float state, boolean isLeaving) {
        back.setVisibility(state == 0f ? View.INVISIBLE : View.VISIBLE);
        back.getBackground().setAlpha((int) (255 * state));

        vShadow.getBackground().setAlpha((int) (255 * state));
        vShadow.setVisibility(state == 0f ? View.INVISIBLE : View.VISIBLE);

        toolbar.setVisibility(state == 0f ? View.INVISIBLE : View.VISIBLE);
        if (Build.VERSION.SDK_INT > 10)
            toolbar.setAlpha(state);

        Log.d(getClass().getName(), "onPositionUpdate: " + state);

        if (isLeaving && state == 0f) {
            flicAdapter.setActivated(false);
        }
    }

    public void onDestroy() {
        if(flicAdapter!=null)
            flicAdapter.removePositionAnimation(this);
    }

    public boolean onBackPressed() {
        if (flicAdapter != null && !flicAdapter.getAnimator().isLeaving()) {
            flicAdapter.getAnimator().exit(true);
            return true;
        }
        return false;
    }

    public void setPhotos(List<? extends ZoomPhotoPagerAdapter.IPhoto> data) {
        zoomPager.setAdapter(flicAdapter);
        flicAdapter.setPhotos(data);
    }

    public ZoomImageController setPhotos(ImageView from, List<? extends ZoomPhotoPagerAdapter.IPhoto> data) {
        initFlickrAdapter();
        flicAdapter.from(from);
        setPhotos(data);
        return this;
    }

    public ZoomImageController setPhotos(List<ImageView> from, List<? extends ZoomPhotoPagerAdapter.IPhoto> data) {
        initFlickrAdapter();
        flicAdapter.from(from);
        setPhotos(data);
        return this;
    }

    public ZoomImageController setPhotos(ViewPager from, List<? extends ZoomPhotoPagerAdapter.IPhoto> data) {
        initFlickrAdapter();
        flicAdapter.from(from);
        setPhotos(data);
        return this;
    }

    public ZoomImageController setPhotos(RecyclerView from, List<? extends ZoomPhotoPagerAdapter.IPhoto> data) {
        initFlickrAdapter();
        flicAdapter.from(from);
        setPhotos(data);
        return this;
    }

    public void enter(int position) {
        flicAdapter.setActivated(true);
        flicAdapter.getAnimator().enter(position, true);
    }

    protected void initDecorMargins() {
        // Adjusting margins and paddings to fit translucent decor
        if(Build.VERSION.SDK_INT >= 16 ) {
        if(root.getChildAt(0).getFitsSystemWindows())
            DecorUtils.marginForStatusBar(toolbar);
        }
    }
}
