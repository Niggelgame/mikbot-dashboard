package dev.schlaubi.dashboard.api

import dev.schlaubi.dashboard.models.pageelements.TextElementConfig
import dev.schlaubi.dashboard.models.pageelements.TextElementOptions

class TextContext(val value: ValueProvider<*, String>)  : PageElementModuleContext<TextElementConfig>() {
    var color: String? = null
    var fontSize: String? = null

    override fun toConfig(): PageModuleConfig<TextElementConfig> {
        return PageModuleConfig(
            config = TextElementConfig(
                value = value.toPageVariable(),
                options = TextElementOptions(
                    color = color,
                    fontSize = fontSize
                )
            ),
            routes = routes,
            variables = variables
        )
    }
}

