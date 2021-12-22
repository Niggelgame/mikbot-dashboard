package dev.schlaubi.dashboard.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


// Variable receiving
@Serializable
sealed class PageVariable

@Serializable
@SerialName("direct")
data class DirectPageVariable(val value: String) : PageVariable()

@Serializable
@SerialName("var")
data class VarPageVariable(val value: String, val placeholder: String? = null, val property: List<String>? = null) :
    PageVariable()
