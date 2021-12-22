package dev.schlaubi.dashboard.models.pageelements

import dev.schlaubi.dashboard.models.PageVariable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("textinput")
data class TextInputElementConfig(
    @SerialName("input_identifier") val inputIdentifier: String,
    val label: String? = null,
    val default: PageVariable? = null,
    val placeholder: PageVariable? = null,
    val required: Boolean? = null,
    val options: TextInputOptions? = null,
    @SerialName("input_validation") val inputValidation: List<TextInputValidation>? = null
) : PageElementConfig()

@Serializable
data class TextInputOptions(val type: String? = null)

@Serializable
enum class TextInputValidationType {
    @SerialName("regex") REGEX,
    @SerialName("min_length") MIN_LENGTH,
    @SerialName("eq_input") EQ_INPUT
}

@Serializable
data class TextInputValidation(val type: TextInputValidationType, val value: String)
