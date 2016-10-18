package zoom.mcgars.com.zoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import mcgars.com.zoomimage.ZoomImageController;
import mcgars.com.zoomimage.adapter.ZoomPhotoPagerAdapter;
import mcgars.com.zoomimage.ui.Displayer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ZoomImageController zoomImageController;
    private ImageView ivTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initImageLoader(MainActivity.this);

        ivTest = (ImageView) findViewById(R.id.ivTest);

        zoomImageController = new ZoomImageController(this, new Displayer() {
            // load fullscreen image
            @Override
            public void displayImage(ZoomPhotoPagerAdapter.IPhoto photo, ZoomPhotoPagerAdapter.ViewHolder v) {
                displayImg(photo, v.image, v);
            }

            // load preview image
            @Override
            public void displayImage(ZoomPhotoPagerAdapter.IPhoto photo, ImageView v) {
                displayImg(photo, v, null);
            }

            @Override
            public void cancel(ImageView v) {
                ImageLoader.getInstance().cancelDisplayTask(v);
            }
        });


        ImageLoader.getInstance().displayImage("https://graceologydotcom.files.wordpress.com/2014/04/perma-glory.jpg", ivTest);
        ivTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        List<ZoomPhotoPagerAdapter.IPhoto> list = new ArrayList<>();
        list.add(new ZoomPhotoPagerAdapter.Photo(
                "https://graceologydotcom.files.wordpress.com/2014/04/perma-glory.jpg",
                "https://graceologydotcom.files.wordpress.com/2014/04/perma-glory.jpg"
        ));
        zoomImageController.setPhotos(ivTest, list);
        zoomImageController.enter(0);
    }

    /**
     * Display
     * @param photo
     * @param imageView
     * @param v
     */
    private void displayImg(ZoomPhotoPagerAdapter.IPhoto photo, ImageView imageView, final ZoomPhotoPagerAdapter.ViewHolder v) {
        ImageLoader.getInstance().displayImage(photo.getOriginal(), imageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(v!=null)
                    v.loadComplete();
            }
        });
    }

    /**
     * Init loader images
     * @param c
     * @return
     */
    public static ImageLoader initImageLoader(Context c) {
        if (ImageLoader.getInstance().isInited())
            return ImageLoader.getInstance();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(c)
                .defaultDisplayImageOptions(getImageLoaderOptions())
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // 2 Mb
                .build();

        ImageLoader.getInstance().init(config);
        return ImageLoader.getInstance();
    }

    public static DisplayImageOptions getImageLoaderOptions() {
        return getImageLoaderOptionsBuilder().build();
    }

    public static DisplayImageOptions.Builder getImageLoaderOptionsBuilder() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(false);
    }

}
