package mcgars.com.zoomimage

import androidx.viewpager.widget.ViewPager
import com.alexvasilkov.gestures.animation.ViewPositionAnimator
import mcgars.com.zoomimage.adapter.ZoomPhotoPagerAdapter
import mcgars.com.zoomimage.fabric.AMBuilder
import mcgars.com.zoomimage.model.IPhoto
import mcgars.com.zoomimage.ui.Displayer
import mcgars.com.zoomimage.ui.UiContainer

class ZoomImageController(
        private val uiContainer: UiContainer,
        private val displayer: Displayer,
        private val titleTransformer: ((Int, Int, IPhoto?) -> CharSequence)? = null
) : ViewPositionAnimator.PositionUpdateListener, ViewPager.OnPageChangeListener {

    private val zoomAdapter: ZoomPhotoPagerAdapter by lazy {
        ZoomPhotoPagerAdapter(uiContainer.zoomPager, displayer, this)
    }

    init {
        uiContainer.zoomPager.addOnPageChangeListener(this)
        uiContainer.zoomToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * Animate cosmetics views (ViewPager, Toolbar, Background)
     * @param state float
     * @param isLeaving boolean
     */
    override fun onPositionUpdate(state: Float, isLeaving: Boolean) {
        uiContainer.show(invisible = state == 0f, alpha = (255 * state).toInt())

        if (isLeaving && state == 0f) {
            zoomAdapter.setActivated(false)
        }
    }

    fun onDestroy() {
        uiContainer.zoomPager.removeOnPageChangeListener(this)
        zoomAdapter.removePositionAnimation()
    }

    /**
     * If image is full size closed it
     * @return if image is open true else false
     */
    fun onBackPressed(): Boolean {
        return zoomAdapter.exit(true)
    }

    /**
     * Before [.show] need set data and builder
     *
     * @param builder set from views
     * @param data images
     * @return this
     */
    fun setPhotos(builder: AMBuilder, data: List<IPhoto>): ZoomImageController {
        zoomAdapter.from(builder)
        setPhotos(data)
        return this
    }

    private fun setPhotos(data: List<IPhoto>) {
        uiContainer.zoomPager.adapter = zoomAdapter
        zoomAdapter.setPhotos(data)
    }

    /**
     * Zoom image to front
     *
     * @param position by
     */
    fun show(position: Int = 0) {
        zoomAdapter.setActivated(true)
        zoomAdapter.enter(position, true)
        // fixed, if firs page onPageSelected not trigger
        if (position == 0) setTitle(position)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        setTitle(position)
    }

    private fun setTitle(position: Int) {
        val item = zoomAdapter.getPhoto(position)

        val title = titleTransformer?.invoke(position, zoomAdapter.count, item) ?: when {
            zoomAdapter.count <= 1 -> item?.text
            else -> createDefaultTitle(position, item)
        }

        uiContainer.zoomToolbar.title = title
    }

    private fun createDefaultTitle(position: Int, item: IPhoto?): String {
        val text = (position + 1).toString() + " / " + zoomAdapter.count
        return when {
            item?.text.isNullOrEmpty().not() -> "(" + text + ") " + item?.text
            else -> text
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}
