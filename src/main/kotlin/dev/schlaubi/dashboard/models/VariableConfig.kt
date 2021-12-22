package dev.schlaubi.dashboard.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Variable definition
@Serializable
sealed class VariableConfig {
    abstract val id: String
}

@Serializable
@SerialName("direct")
data class DirectVariableConfig(
    override val id: String, val value: String
) : VariableConfig()

@Serializable
@SerialName("request")
data class RequestVariableConfig(
    override val id: String,
    val lazy: Boolean? = null,
    val url: String,
    val method: String? = null,
    val body: String? = null,
    val headers: Map<String, String>? = null,
    val placeholder: String? = null
) : VariableConfig()