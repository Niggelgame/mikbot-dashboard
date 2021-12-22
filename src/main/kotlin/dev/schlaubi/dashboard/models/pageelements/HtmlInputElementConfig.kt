package dev.schlaubi.dashboard.models.pageelements

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("rawhtml")
data class HtmlInputElementConfig(
    @SerialName("raw_html") val html: String
) : PageElementConfig()
