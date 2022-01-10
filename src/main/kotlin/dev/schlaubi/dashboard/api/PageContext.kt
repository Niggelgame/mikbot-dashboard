package dev.schlaubi.dashboard.api

import dev.schlaubi.dashboard.models.PageConfig

class PageContext(private val name: String) : ModuleContext<PageConfig>() {
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