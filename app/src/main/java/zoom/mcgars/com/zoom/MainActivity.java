package zoom.mcgars.com.zoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
import mcgars.com.zoomimage.fabric.AnimatorBuilder;
import mcgars.com.zoomimage.ui.Displayer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ZoomImageController zoomImageController;

    List<ZoomPhotoPagerAdapter.IPhoto> urlImages = new ArrayList<>();
    List<ImageView> views = new ArrayList<>();
    private ViewGroup container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (ViewGroup) findViewById(R.id.container);

        initImageLoader(this);

        fitImages();

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


//        ImageLoader.getInstance().displayImage("https://graceologydotcom.files.wordpress.com/2014/04/perma-glory.jpg", ivTest);
    }

    private void fitImages() {
        urlImages.add(new ZoomPhotoPagerAdapter.Photo(
                "http://images4.fanpop.com/image/photos/23100000/-Nature-god-the-creator-23175770-670-446.jpg",
                "http://images4.fanpop.com/image/photos/23100000/-Nature-god-the-creator-23175770-670-446.jpg"
        ));
        urlImages.add(new ZoomPhotoPagerAdapter.Photo(
                "http://fantasyartdesign.com/free-wallpapers/imgs/mid/decktop-Dino-game-m.jpg",
                "http://fantasyartdesign.com/free-wallpapers/imgs/mid/decktop-Dino-game-m.jpg"
        ));
        urlImages.add(new ZoomPhotoPagerAdapter.Photo(
                "http://images4.fanpop.com/image/photos/18200000/Lovely-nature-god-the-creator-18227423-500-333.jpg",
                "http://images4.fanpop.com/image/photos/18200000/Lovely-nature-god-the-creator-18227423-500-333.jpg"
        ));

        views.add((ImageView) findViewById(R.id.ivTest));
        views.add((ImageView) findViewById(R.id.ivTest2));
        views.add((ImageView) findViewById(R.id.ivTest3));

        for (int i = 0; i < views.size(); i++) {
            ImageView view = views.get(i);
            view.setOnClickListener(this);
            ImageLoader.getInstance().displayImage(urlImages.get(i).getPreview(), view);
        }
    }

    @Override
    public void onClick(View v) {
        int position = container.indexOfChild(v);
        zoomImageController.setPhotos(AnimatorBuilder.getInstance().from(views), urlImages);
        zoomImageController.show(position);
    }

    @Override
    public void onBackPressed() {
        if(zoomImageController.onBackPressed())
            return;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        zoomImageController.onDestroy();
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
