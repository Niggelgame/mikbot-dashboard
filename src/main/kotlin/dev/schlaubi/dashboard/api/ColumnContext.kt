package dev.schlaubi.dashboard.api

import dev.schlaubi.dashboard.models.pageelements.ColumnElementConfig

fun ModuleContext<*>.column(cb: ColumnContext.() -> Unit) {
    val column = ColumnContext()
    column.cb()
    modules.add(column)
}

class ColumnContext : PageElementModuleContext<ColumnElementConfig>() {
    var mainAxis: String? = null
    var crossAxis: String? = null

    override fun toConfig(): PageModuleConfig<ColumnElementConfig> {
        val allConfigs = modules.map { it.toConfig() }
        val allVariables = allConfigs.flatMap { it.variables }
        val allElements = allConfigs.map { it.config }
        val allRoutes = allConfigs.flatMap { it.routes }

        return PageModuleConfig(
            config = ColumnElementConfig(
                elements = allElements,
                crossAxis = crossAxis,
                mainAxis = mainAxis
            ),
            variables = variables + allVariables,
            routes = routes + allRoutes
        )
    }
}