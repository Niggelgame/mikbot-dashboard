package dev.schlaubi.dashboard.api

import ActionContext
import dev.schlaubi.dashboard.models.pageelements.ButtonInputElementConfig
import dev.schlaubi.dashboard.models.pageelements.ButtonInputOption
import dev.schlaubi.dashboard.models.pageelements.ButtonSubmitAction

class ButtonContext(private val label: String) : PageElementModuleContext<ButtonInputElementConfig>() {
    private var action: ActionContext? = null
    var height: String? = null
    var width: String? = null
    var color: String? = null

    fun action(cb: ActionContext.() -> Unit) {
        action = ActionContext()
        action!!.cb()
    }

    override fun toConfig(): PageModuleConfig<ButtonInputElementConfig> {
        if (action == null) throw IllegalStateException("Action is not set")
        val actionConfig = action!!.toConfig()
        return PageModuleConfig(
            config = ButtonInputElementConfig(
                action = actionConfig.let {
                    ButtonSubmitAction(
                        endpoint = it.routes.first().path,
                        method = "POST",
                        values = it.config
                    )
                },
                options = ButtonInputOption(
                    height = height,
                    width = width,
                    color = color
                ),
                label = label
            ),
            routes = actionConfig.routes + routes,
            variables = variables
        )
    }
}
