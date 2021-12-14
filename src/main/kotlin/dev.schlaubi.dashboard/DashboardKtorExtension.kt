package dev.schlaubi.dashboard

import dev.schlaubi.mikbot.util_plugins.ktor.api.KtorExtensionPoint
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.pf4j.Extension

@Extension
class DashboardKtorExtension : KtorExtensionPoint {
    override fun Application.apply() {
        routing {
            get("/") {
                call.respondText("Hello World!")
            }
        }
    }
}