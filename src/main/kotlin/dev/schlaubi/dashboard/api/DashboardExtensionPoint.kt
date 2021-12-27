package dev.schlaubi.dashboard.api

import org.pf4j.ExtensionPoint

interface DashboardExtensionPoint : ExtensionPoint {
    val moduleName: String

    /**
     * Registration method for the module pages
     */
    fun DashboardModuleContext.register()
}