package mcgars.com.zoomimage.fabric

import com.alexvasilkov.gestures.transition.ViewsTransitionBuilder

interface AMBuilder {
    fun build(): ViewsTransitionBuilder<Int>
}
