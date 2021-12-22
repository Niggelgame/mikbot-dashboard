package dev.schlaubi.dashboard.models.pageelements

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("row")
data class RowElementConfig(
    val elements: List<PageElementConfig>,
    @SerialName("cross_axis") val crossAxis: String? = null,
    @SerialName("main_axis") val mainAxis: String? = null,
) : PageElementConfig()