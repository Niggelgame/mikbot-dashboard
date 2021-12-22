package dev.schlaubi.dashboard

import dev.schlaubi.mikbot.util_plugins.ktor.api.KtorExtensionPoint
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json
import org.pf4j.Extension

@Extension
class DashboardKtorExtension : KtorExtensionPoint {
    override fun Application.apply() {
        /*install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
            })
        }*/
        routing {
            get("/") {
                call.respondText("Hello World!")
            }
        }
    }
}