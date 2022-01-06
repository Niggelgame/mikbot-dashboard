package dev.schlaubi.dashboard.models.pageelements

import dev.schlaubi.dashboard.models.PageVariable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("textinput")
data class TextInputElementConfig(
    val controller: TextInputController,
    val label: String? = null,
    val placeholder: PageVariable? = null,
    val options: TextInputOptions? = null,
) : PageElementConfig()

@Serializable
data class TextInputController(
    @SerialName("input_identifier") val inputIdentifier: String,
    @SerialName("binding_variable") val bindingVariable: PageVariable,
    @SerialName("input_validation") val inputValidation: List<TextInputValidation>? = null,
    val required: Boolean = true,
)

@Serializable
data class TextInputOptions(val type: String? = null)

@Serializable
enum class TextInputValidationType {
    @SerialName("regex")
    REGEX,

    @SerialName("min_length")
    MIN_LENGTH,

    @SerialName("eq_input")
    EQ_INPUT
}

@Serializable
data class TextInputValidation(val type: TextInputValidationType, val value: String)
