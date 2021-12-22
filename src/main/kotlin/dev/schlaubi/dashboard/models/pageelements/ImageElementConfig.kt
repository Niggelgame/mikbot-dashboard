package dev.schlaubi.dashboard.models.pageelements

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("image")
data class ImageElementConfig(
    val url: String, val alt: String? = null, val options: ImageElementOptions? = null
) : PageElementConfig()

@Serializable
data class ImageElementOptions(
    val width: String? = null, val height: String? = null, val caption: String? = null
)