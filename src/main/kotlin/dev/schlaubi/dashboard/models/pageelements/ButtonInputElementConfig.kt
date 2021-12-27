package dev.schlaubi.dashboard.models.pageelements

import dev.schlaubi.dashboard.models.ActionValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("button")
data class ButtonInputElementConfig(
    val label: String, val options: ButtonInputOption?, val action: ButtonAction
) : PageElementConfig()

@Serializable
data class ButtonInputOption(
    val color: String? = null,
    val width: String? = null,
    val height: String? = null,
)

@Serializable
sealed class ButtonAction

@Serializable
@SerialName("submit")
data class ButtonSubmitAction(
    val endpoint: String, val method: String? = null, val values: List<ActionValue>? = null
) : ButtonAction()


