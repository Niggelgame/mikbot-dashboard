package dev.schlaubi.dashboard.models

import dev.schlaubi.dashboard.models.pageelements.PageElementConfig
import kotlinx.serialization.Serializable

@Serializable
data class PageConfig(
    val name: String,
    val elements: List<PageElementConfig>,
    val variables: List<VariableConfig>
)
