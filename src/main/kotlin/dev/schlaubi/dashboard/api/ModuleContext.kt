package dev.schlaubi.dashboard.api

import ActionContext
import dev.schlaubi.dashboard.models.*
import dev.schlaubi.dashboard.models.pageelements.PageElementConfig
import dev.schlaubi.dashboard.models.pageelements.TextInputController
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.litote.kmongo.path
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class ActionRoute(val path: String, val action: suspend (JsonElement) -> ActionRouteResult) {
    @Serializable
    data class ActionRouteResult(val data: String, @SerialName("variable_updates") val variableUpdates:  Map<String, ActionContext.VariableUpdate> = mapOf())

    fun Route.register() {
        post(path) {
            val json = try {
                call.receive<JsonElement>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid data provided")
                return@post
            }

            val result = try {
                action(json)
            } catch (e: ActionContext.InvalidContentException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid data provided")
                return@post
            }
            // call.response.headers.append(HttpHeaders.AccessControlAllowOrigin, "*")
            call.respond(result)
        }
    }
}

data class PageModuleConfig<T>(
    val config: T, val routes: List<ActionRoute>, val variables: List<VariableConfig>
)

abstract class ModuleContext<T> {
    val variables = mutableListOf<VariableConfig>()
    val routes = mutableListOf<ActionRoute>()
    // Child Modules
    val modules = mutableListOf<PageElementModuleContext<*>>()
    abstract fun toConfig(): PageModuleConfig<T>

    fun <T : Any> newVariable(value: T): VariableData<T> {
        val id = UUID.randomUUID().toString()
        val variable = VariableData<T>(id)
        variables.add(DirectVariableConfig(id, value.toString()))
        return variable
    }

    inline fun <reified T : Any> newFutureVariable(
        lazy: Boolean = true, name: String? = null, crossinline cb: suspend () -> T
    ): VariableData<T> {
        val id = UUID.randomUUID().toString()
        val endpoint = "/variables/$id"
        val route = ActionRoute(endpoint) {
            val result = cb()
            ActionRoute.ActionRouteResult(Json.encodeToString(result))
        }
        routes.add(route)
        variables.add(
            RequestVariableConfig(
                name ?: id, lazy, endpoint, method = "POST"
            )
        )
        return VariableData(id)
    }

    fun <T : Any> newTextInputController(
        bindingVariable: ValueProvider<T, String>, validators: List<TextInputValidator<*>> = listOf()
    ): TextInputControllerContext<T> {
        return TextInputControllerContext(parentVariable = bindingVariable, validators = validators)
    }
}

abstract class PageElementModuleContext<T : PageElementConfig> : ModuleContext<T>()

abstract class ValueProvider<T : Any, R> {
    abstract fun getBaseVariable(): VariableData<T>
    abstract fun getValue(value: T): R

    fun toPageVariable(): PageVariable {
        return when (this) {
            is VariableData<*> -> VarPageVariable(this.id)
            is VariableDataChild<*, *> -> VarPageVariable(this.parentVariable.id, property = this.path)
            else -> throw IllegalArgumentException("Cannot convert $this to PageVariable")
        }
    }
}

class VariableData<T : Any>(val id: String = UUID.randomUUID().toString()) : ValueProvider<T, T>() {
    override fun getBaseVariable() = this
    override fun getValue(value: T): T = value
}

class VariableDataChild<T : Any, R>(val parentVariable: VariableData<T>, val path: List<String>) :
    ValueProvider<T, R>() {
    @Suppress("UNCHECKED_CAST")
    override fun getValue(value: T): R {
        var current: Any = value
        for (path in path) {
            val property = current::class.memberProperties.find { it.name == path } as KProperty1<Any, *>?
                ?: throw IndexOutOfBoundsException("Property $path not found in ${current::class.qualifiedName}")

            // Make sure the property is temporarily accessible
            val wasAccessible = property.isAccessible
            if (!wasAccessible) property.isAccessible = true
            current = property.get(current)
                ?: throw IndexOutOfBoundsException("Property $path not found in ${current::class.qualifiedName}")
            if (!wasAccessible) property.isAccessible = false
        }
        return current as R
    }

    override fun getBaseVariable(): VariableData<T> = parentVariable
}

fun <T : Any, R> VariableData<T>.getProperty(property: KProperty1<T, R>): VariableDataChild<T, R> {
    val propertyPath = property.path()
    return VariableDataChild(this, path = propertyPath.split("."))
}

fun <T : Any, R, S> VariableDataChild<T, S>.getProperty(property: KProperty1<S, R>): VariableDataChild<T, R> {
    val propertyPath = property.path()
    return VariableDataChild(this.parentVariable, path = path + propertyPath.split("."))
}