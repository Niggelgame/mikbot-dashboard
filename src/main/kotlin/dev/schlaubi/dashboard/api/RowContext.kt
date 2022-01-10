package dev.schlaubi.dashboard.api

import dev.schlaubi.dashboard.models.pageelements.RowElementConfig

fun ModuleContext<*>.row(cb: RowContext.() -> Unit) {
    val row = RowContext()
    row.cb()
    modules.add(row)
}

class RowContext : PageElementModuleContext<RowElementConfig>() {
    var mainAxis: String? = null
    var crossAxis: String? = null

    override fun toConfig(): PageModuleConfig<RowElementConfig> {
        val allConfigs = modules.map { it.toConfig() }
        val allVariables = allConfigs.flatMap { it.variables }
        val allElements = allConfigs.map { it.config }
        val allRoutes = allConfigs.flatMap { it.routes }

        return PageModuleConfig(
            config = RowElementConfig(
                elements = allElements,
                crossAxis = crossAxis,
                mainAxis = mainAxis
            ),
            variables = variables + allVariables,
            routes = routes + allRoutes
        )
    }
}