package dev.schlaubi.dashboard.api

import dev.schlaubi.dashboard.models.PageConfig

class PageContext(private val name: String) : ModuleContext<PageConfig>() {
    private val modules = mutableListOf<PageElementModuleContext<*>>()

    fun button(label: String, cb: ButtonContext.() -> Unit) {
        modules.add(ButtonContext(label).apply(cb))
    }

    fun text(value: ValueProvider<*, String>, cb: (TextContext.() -> Unit)? = null) {
        val text = TextContext(value)
        if(cb != null) text.apply(cb)
        modules.add(text)
    }

    fun text(value: String, cb: (TextContext.() -> Unit)? = null) {
        val text = TextContext(newVariable(value))
        if(cb != null) text.apply(cb)
        modules.add(text)
    }

    fun textInput(controllerContext: TextInputControllerContext<*>, cb: (TextInputContext.() -> Unit)? = null) {
        val textInput = TextInputContext(controllerContext)
        if(cb != null) textInput.apply(cb)
        modules.add(textInput)
    }

    override fun toConfig(): PageModuleConfig<PageConfig> {
        val allConfigs = modules.map { it.toConfig() }
        val allVariables = allConfigs.flatMap { it.variables }
        val allElements = allConfigs.map { it.config }
        val allRoutes = allConfigs.flatMap { it.routes }

        return PageModuleConfig(
            config = PageConfig(
                name = name,
                variables = allVariables + variables,
                elements = allElements
            ),
            routes = allRoutes + routes,
            variables = allVariables + variables,
        )
    }
}