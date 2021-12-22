package dev.schlaubi.dashboard.models.pageelements

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("column")
data class ColumnElementConfig(
    @SerialName("main_axis") val mainAxis: String? = null,
    @SerialName("cross_axis") val crossAxis: String? = null,
    val elements: List<PageElementConfig>
) : PageElementConfig()
