package dev.schlaubi.dashboard.api

import dev.schlaubi.dashboard.models.*
import dev.schlaubi.dashboard.models.pageelements.PageElementConfig
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.json.JsonElement
import java.util.*

class ActionRoute(val path: String, val action: suspend (JsonElement) -> Any) {
    fun Route.register() {
        post(path) {
            val json = call.receive<JsonElement>()
            val result = action(json)
            call.respond(result)
        }
    }
}

data class PageModuleConfig<T>(
    val config: T,
    val routes: List<ActionRoute>,
    val variables: List<VariableConfig>
)

abstract class ModuleContext<T> {
    val variables = mutableListOf<VariableConfig>()
    val routes = mutableListOf<ActionRoute>()
    abstract fun toConfig(): PageModuleConfig<T>

    fun <T : Any> newVariable(value: T, name: String? = null): VariableData<T> {
        val id = UUID.randomUUID().toString()
        val variable = VariableData<T>(name ?: id, id)
        variables.add(DirectVariableConfig(name ?: id, value.toString()))
        return variable
    }

    fun newBinding(name: String?): BindingData {
        val id = UUID.randomUUID().toString()
        return BindingData(name ?: id, id)
    }

    fun <T : Any> newFutureVariable(cb: suspend () -> T, lazy: Boolean = true, name: String? = null): VariableData<T> {
        val id = UUID.randomUUID().toString()
        val endpoint = "/variables/$id"
        val route = ActionRoute(endpoint) {
            cb()
        }
        routes.add(route)
        variables.add(
            RequestVariableConfig(
                name ?: id,
                lazy,
                endpoint,
                method = "POST"
            )
        )
        return VariableData(name ?: id, id)
    }
}

abstract class PageElementModuleContext<T : PageElementConfig> : ModuleContext<T>()

class VariableData<T : Any>(val name: String, val id: String = UUID.randomUUID().toString())

class BindingData(val identifier: String, val id: String = UUID.randomUUID().toString())