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
@SerialName("binding")
data class ActionValueBinding(
    override val key: String, val identifier: String
) : ActionValue()

