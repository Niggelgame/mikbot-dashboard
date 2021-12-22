package dev.schlaubi.dashboard.models

import kotlinx.serialization.Serializable

@Serializable
data class ModuleConfig(
    val name: String,
    val pages: List<PageConfig>
)
