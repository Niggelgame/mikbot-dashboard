package dev.schlaubi.dashboard.models.pageelements

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("spacer")
data class SpacerElementConfig(
    val flex: Int? = null, val child: PageElementConfig? = null
) : PageElementConfig()
