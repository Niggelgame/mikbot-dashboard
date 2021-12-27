package dev.schlaubi.dashboard

import dev.schlaubi.dashboard.api.ActionRoute
import dev.schlaubi.dashboard.api.DashboardExtensionPoint
import dev.schlaubi.dashboard.api.DashboardModuleContext
import dev.schlaubi.dashboard.models.BaseConfig
import dev.schlaubi.dashboard.models.ModuleConfig
import dev.schlaubi.dashboard.models.sampleData
import dev.schlaubi.mikbot.plugin.api.getExtensions
import dev.schlaubi.mikbot.plugin.api.pluginSystem
import dev.schlaubi.mikbot.util_plugins.ktor.api.KtorExtensionPoint
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import org.pf4j.Extension

private const val DASHBOARD_BASE_PATH = "/dashboard"

@Extension
class DashboardKtorExtension : KtorExtensionPoint {
    override fun Application.apply() {
        install(CORS) {
            anyHost()
        }

        routing {
            get("/") {
                call.respond(sampleData)
            }

            // TODO: Add built frontend to resources, bundle it with the plugin and serve it here
            static("$DASHBOARD_BASE_PATH/") {
                default("index.html")
            }

            route("$DASHBOARD_BASE_PATH/api") {
                with(DashboardProvider) {
                    dashboard()
                }
            }
        }
    }

    companion object DashboardProvider {
        private val extensions = pluginSystem.getExtensions<DashboardExtensionPoint>()
        private lateinit var modules: List<DashboardModuleContext>
        private val routes = mutableListOf<ActionRoute>()
        private val configs = mutableListOf<ModuleConfig>()

        private fun ensureInitialized() {
            modules = extensions.map {
                val module = DashboardModuleContext(it.moduleName)
                with(it) {
                    module.register()
                }
                module
            }

            modules.forEach {
                val conf = it.toConfig()
                routes.addAll(conf.routes)
                configs.add(conf.config)
            }
        }

        fun Route.dashboard() {
            ensureInitialized()

            get("/config") {
                call.respond(BaseConfig(configs))
            }

            routes.forEach { route ->
                with(route) {
                    register()
                }
            }
        }
    }
}
