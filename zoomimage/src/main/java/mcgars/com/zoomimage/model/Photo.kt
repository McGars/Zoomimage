package mcgars.com.zoomimage.model

data class Photo(
        override val preview: String,
        override val original: String,
        override val text: String? = null
) : IPhoto