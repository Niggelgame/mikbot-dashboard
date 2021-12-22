package dev.schlaubi.dashboard.models.pageelements

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("sizedbox")
data class SizedBoxElementConfig(
    val height: String? = null, val width: String? = null
) : PageElementConfig()
