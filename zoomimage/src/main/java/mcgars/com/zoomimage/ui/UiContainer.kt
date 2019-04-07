package mcgars.com.zoomimage.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import mcgars.com.zoomimage.R


class UiContainer(
        private val rootContainer: ViewGroup,
        modifier: (View.() -> Unit)? = null
) {

    internal val zoomPager: ViewPager by lazy { rootContainer.findViewById<ViewPager>(R.id.zoomFullPager) }


    val zoomToolbar: Toolbar by lazy {
        rootContainer.findViewById<Toolbar>(R.id.zoomFullToolbar).apply {
            setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        }
    }

    private val vShadow: View by lazy { rootContainer.findViewById<View>(R.id.vZoomShadow) }

    private val back: View by lazy { rootContainer.findViewById<View>(R.id.zoomFullBackground) }

    init {
        val inflater = LayoutInflater.from(rootContainer.context)
        val v = inflater.inflate(R.layout.zoom_view_image, rootContainer, true)
        afterInflate()
        modifier?.invoke(v)
    }

    internal fun show(invisible: Boolean, alpha: Int) {
        val isInvisible = if (invisible) View.INVISIBLE else View.VISIBLE

        back.visibility = isInvisible
        back.background.alpha = alpha

        vShadow.visibility = isInvisible
        vShadow.background.alpha = alpha

        zoomToolbar.visibility = isInvisible
        zoomToolbar.alpha = alpha.toFloat() / 255f
    }

    private fun afterInflate() {
        if (rootContainer.fitsSystemWindows) {
            arrayOf(back, vShadow, zoomPager).forEach {
                it.fitsSystemWindows = true
            }
        }
    }

}