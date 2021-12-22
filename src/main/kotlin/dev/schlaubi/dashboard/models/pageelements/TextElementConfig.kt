package dev.schlaubi.dashboard.models.pageelements

import dev.schlaubi.dashboard.models.PageVariable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("text")
data class TextElementConfig(
    val value: PageVariable,
    val options: TextElementOptions? = null,
) : PageElementConfig()

@Serializable
data class TextElementOptions(
    val color: String? = null, @SerialName("font_size") val fontSize: String? = null
)