package dev.schlaubi.dashboard

import dev.schlaubi.dashboard.api.ActionRoute
import dev.schlaubi.dashboard.api.DashboardExtensionPoint
import dev.schlaubi.dashboard.api.DashboardModuleContext
import dev.schlaubi.dashboard.models.BaseConfig
import dev.schlaubi.dashboard.models.ModuleConfig
import dev.schlaubi.mikbot.plugin.api.getExtensions
import dev.schlaubi.mikbot.plugin.api.pluginSystem
import dev.schlaubi.mikbot.util_plugins.ktor.api.KtorExtensionPoint
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable
import org.litote.kmongo.div
import org.litote.kmongo.path
import org.litote.kmongo.property.KPropertyPath
import org.pf4j.Extension

private const val DASHBOARD_BASE_PATH = "/dashboard"

@Serializable
data class Test(val a: String, val b: Test2, val c: String)

@Serializable
data class Test2(val f: Test3)

@Serializable
data class Test3(val g: String)

@Extension
class DashboardKtorExtension : KtorExtensionPoint {
    override fun Application.apply() {
        install(CORS) {
            /*
            allowHeaders { true }*/
            anyHost()
            allowSameOrigin = true
            HttpMethod.DefaultMethods.forEach { method(it) }
            header(HttpHeaders.Authorization)
            header(HttpHeaders.ContentType)
        }

        routing {
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
                // call.response.headers.append(HttpHeaders.AccessControlAllowOrigin, "*")
                call.respond(BaseConfig(configs))
            }

            routes.forEach { route ->
                with(route) {
                    println("Registering route: $path")
                    register()
                }
            }
        }
    }
}
