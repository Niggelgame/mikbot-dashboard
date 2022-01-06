package dev.schlaubi.dashboard.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ActionValue {
    // Key used in request body
    abstract val key: String
}

@Serializable
@SerialName("var")
data class ActionValueVariable(
    override val key: String, val variable: String
) : ActionValue()

@Serializable
@SerialName("controller")
data class ActionValueController(
    override val key: String, val variable: String
) : ActionValue()