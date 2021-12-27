package dev.schlaubi.dashboard

import dev.schlaubi.dashboard.api.DashboardExtensionPoint
import dev.schlaubi.dashboard.api.DashboardModuleContext
import dev.schlaubi.dashboard.api.VariableData
import org.pf4j.Extension

@Extension
class TestModule : DashboardExtensionPoint {
    override val moduleName: String = "TestModule"

    override fun DashboardModuleContext.register() {


        page("Test") {
            val variable = newVariable("test")

            button {
                action {
                    useVariable(variable)

                    run {
                        val value = fromVariable(variable)
                        println("Value: $value")
                    }
                }
            }
        }
    }
}

