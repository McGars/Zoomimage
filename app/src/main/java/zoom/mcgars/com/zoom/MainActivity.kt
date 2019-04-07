package zoom.mcgars.com.zoom

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener

import mcgars.com.zoomimage.ZoomImageController
import mcgars.com.zoomimage.adapter.ZoomViewHolder
import mcgars.com.zoomimage.fabric.AnimatorBuilder
import mcgars.com.zoomimage.model.IPhoto
import mcgars.com.zoomimage.model.Photo
import mcgars.com.zoomimage.ui.Displayer
import mcgars.com.zoomimage.ui.UiContainer

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var zoomImageController: ZoomImageController

    private val urlImages = listOf<IPhoto>(
            Photo(
                    "http://images4.fanpop.com/image/photos/23100000/-Nature-god-the-creator-23175770-670-446.jpg",
                    "http://fantasyartdesign.com/free-wallpapers/imgs/mid/decktop-Dino-game-m.jpg"
            ),
            Photo(
                    "http://fantasyartdesign.com/free-wallpapers/imgs/mid/decktop-Dino-game-m.jpg",
                    "http://images4.fanpop.com/image/photos/18200000/Lovely-nature-god-the-creator-18227423-500-333.jpg"
            ),
            Photo(
                    "http://images4.fanpop.com/image/photos/18200000/Lovely-nature-god-the-creator-18227423-500-333.jpg",
                    "http://images4.fanpop.com/image/photos/18200000/Lovely-nature-god-the-creator-18227423-500-333.jpg"
            )
    )

    private var views = mutableListOf<ImageView>()

    private val container: ViewGroup by lazy { findViewById<ViewGroup>(R.id.container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        zoomImageController = ZoomImageController(UiContainer(findViewById(R.id.rootView)), object : Displayer {
            // load fullscreen image
            override fun displayImage(photo: IPhoto?, v: ZoomViewHolder) {
                displayImg(photo ?: return, v)
            }

            override fun cancel(imageView: ImageView) {
                ImageLoader.getInstance().cancelDisplayTask(imageView)
            }
        })

        initImageLoader(this)

        fitImages()

        //        ImageLoader.getInstance().displayImage("https://graceologydotcom.files.wordpress.com/2014/04/perma-glory.jpg", ivTest);
    }

    private fun fitImages() {
        views.add(findViewById<View>(R.id.ivTest) as ImageView)
        views.add(findViewById<View>(R.id.ivTest2) as ImageView)
        views.add(findViewById<View>(R.id.ivTest3) as ImageView)

        views.forEachIndexed { index, imageView ->
            imageView.setOnClickListener(this)
            ImageLoader.getInstance().displayImage(urlImages[index].preview, imageView)
        }
    }

    override fun onClick(v: View) {
        val position = container.indexOfChild(v)
        zoomImageController.setPhotos(AnimatorBuilder.from(views), urlImages)
        zoomImageController.show(position)
    }

    override fun onBackPressed() {
        if (zoomImageController.onBackPressed())
            return
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        zoomImageController.onDestroy()
    }

    /**
     * Display
     * @param photo
     * @param v
     */
    private fun displayImg(photo: IPhoto, v: ZoomViewHolder) {
        ImageLoader.getInstance().displayImage(photo.original, v.image, object : SimpleImageLoadingListener() {

            override fun onLoadingStarted(imageUri: String?, view: View?) {

            }

            override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
//                v.loadComplete()
            }
        })
    }

    companion object {

        /**
         * Init loader images
         * @param c
         * @return
         */
        fun initImageLoader(c: Context): ImageLoader {
            if (ImageLoader.getInstance().isInited)
                return ImageLoader.getInstance()

            val config = ImageLoaderConfiguration.Builder(c)
                    .defaultDisplayImageOptions(imageLoaderOptions)
                    .diskCacheSize(50 * 1024 * 1024)
                    .diskCacheFileCount(100)
                    .memoryCache(UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // 2 Mb
                    .build()

            ImageLoader.getInstance().init(config)
            return ImageLoader.getInstance()
        }

        val imageLoaderOptions: DisplayImageOptions
            get() = imageLoaderOptionsBuilder.build()

        val imageLoaderOptionsBuilder: DisplayImageOptions.Builder
            get() = DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .resetViewBeforeLoading(false)
    }

}
