package dev.schlaubi.dashboard.models

import kotlinx.serialization.Serializable

@Serializable
data class BaseConfig(val modules: List<ModuleConfig>)
