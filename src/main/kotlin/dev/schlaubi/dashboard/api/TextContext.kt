package dev.schlaubi.dashboard.api

import dev.schlaubi.dashboard.models.pageelements.TextElementConfig
import dev.schlaubi.dashboard.models.pageelements.TextElementOptions

fun ModuleContext<*>.text(value: ValueProvider<*, String>, cb: (TextContext.() -> Unit)? = null) {
    val text = TextContext(value)
    if(cb != null) text.apply(cb)
    modules.add(text)
}

fun ModuleContext<*>.text(value: String, cb: (TextContext.() -> Unit)? = null) {
    val text = TextContext(newVariable(value))
    if(cb != null) text.apply(cb)
    modules.add(text)
}

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

