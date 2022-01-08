import dev.schlaubi.dashboard.Test
import dev.schlaubi.dashboard.Test3
import dev.schlaubi.dashboard.api.*
import dev.schlaubi.dashboard.models.ActionValue
import dev.schlaubi.dashboard.models.ActionValueController
import dev.schlaubi.dashboard.models.ActionValueVariable
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import kotlinx.serialization.json.Json.Default.encodeToJsonElement
import org.litote.kmongo.MongoOperator
import java.util.*

// https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/json.md#decoding-json-elements
typealias Decoder<T> = (JsonElement) -> T

class ActionContext : ModuleContext<List<ActionValue>>() {
    class InvalidContentException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

    @Serializable
    data class VariableUpdate(
        val value: JsonElement,
        val path: List<String>? = null
    )

    class RunContext(
        private val variableActions: Map<String, ActionData<*>>,
        private val data: JsonElement
    ) {
        val variableUpdates = mutableMapOf<String, VariableUpdate>()

        fun <T : Any> fromVariable(variable: VariableData<T>): T {
            @Suppress("UNCHECKED_CAST")
            val action = (variableActions[variable.id]
                ?: throw IllegalArgumentException("Variable ${variable.id} not found in data. Did you register it with `useVariable`?")) as ActionData<T>


            val entry = data.jsonObject[variable.id]
                ?: throw IllegalArgumentException("Variable ${variable.id} not found in data. This is not supposed to happen. Please report this bug.")

            return try {
                action.decode(entry)
            } catch (e: IndexOutOfBoundsException) {
                throw InvalidContentException()
            } catch (e: SerializationException) {
                throw InvalidContentException()
            }
        }

        fun <T : Any, R> fromVariable(variable: VariableDataChild<T, R>): R {
            val value = fromVariable(variable.parentVariable)
            return variable.getValue(value)
        }

        // Returns value if validation completes successfully
        fun <T : Any> fromTextInputController(
            controllerContext: TextInputControllerContext<T>,
            validated: Boolean = true
        ): String? {
            val baseValue = fromVariable(controllerContext.parentVariable.getBaseVariable())
            val text = controllerContext.parentVariable.getValue(baseValue)
            if (validated) {
                val validationSuccess = controllerContext.validators.all {
                    it.validate(text) { provider -> fromStringVariableProvider(provider) }
                }
                if (!validationSuccess) return null
            }
            return text
        }

        private fun <R : Any> fromStringVariableProvider(provider: ValueProvider<R, String>): String {
            val validator = fromVariable(provider.getBaseVariable())
            return provider.getValue(validator)
        }

        inline fun <reified T : Any> updateVariable(variable: VariableData<T>, value: T) {

            variableUpdates[variable.id] = VariableUpdate(Json.encodeToJsonElement(value))
        }

        inline fun <reified T : Any> updateVariable(variable: VariableDataChild<*, T>, value: T) {
            variableUpdates[variable.parentVariable.id] = VariableUpdate(Json.encodeToJsonElement(value), variable.path)
        }
    }

    data class ActionControllerData<T : Any>(
        val actionData: ActionData<T>,
        val controllerId: String,
    )

    data class ActionData<T : Any>(
        val id: String, val decode: Decoder<T>
    )

    val variableActions = mutableListOf<ActionData<*>>()
    val inputActions = mutableListOf<ActionControllerData<*>>()
    private val runners = mutableListOf<suspend RunContext.() -> Unit>()

    inline fun <reified T : Any> decode(json: JsonElement): T {
        return Json.decodeFromJsonElement(json)
    }

    inline fun <reified T : Any> useVariable(variable: VariableData<T>) {
        variableActions.add(
            ActionData<T>(variable.id, decode = { decode(it) })
        )
    }

    inline fun <reified T : Any> useVariable(variable: VariableDataChild<T, *>) {
        variableActions.add(
            ActionData<T>(variable.parentVariable.id, decode = { decode(it) })
        )
    }

    inline fun <reified T : Any> useTextInputController(controllerContext: TextInputControllerContext<T>) {
        inputActions.add(
            ActionControllerData(
                controllerId = controllerContext.id,
                actionData = ActionData<T>(
                    controllerContext.parentVariable.getBaseVariable().id,
                    decode = { decode(it) })
            )
        )
    }

    fun run(cb: suspend RunContext.() -> Unit) {
        runners.add(cb)
    }

    private suspend fun runWithData(data: JsonElement): Map<String, VariableUpdate> {
        val runContext = RunContext(
            (variableActions + inputActions.map { it.actionData }).associateBy { it.id }, data
        )
        for (runner in runners) {
            runner(runContext)
        }
        return runContext.variableUpdates.toMap()
    }

    override fun toConfig(): PageModuleConfig<List<ActionValue>> {
        val route = "/action/${next()}"
        val actions = variableActions.map {
            ActionValueVariable(it.id, it.id)
        }
        val controllers = inputActions.map {
            // Use controller variable as key and controller id as value
            ActionValueController(it.actionData.id, it.controllerId)
        }
        return PageModuleConfig(
            routes = listOf(ActionRoute(
                route
            ) {
                val variableUpdates = runWithData(it)
                ActionRoute.ActionRouteResult(
                    "",
                    variableUpdates = variableUpdates,
                )
            }) + routes, variables = variables, config = actions + controllers
        )
    }

    companion object ActionUuidGenerator {
        fun next(): String {
            return UUID.randomUUID().toString()
        }
    }
}