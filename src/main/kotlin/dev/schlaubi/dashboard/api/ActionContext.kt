import dev.schlaubi.dashboard.api.*
import dev.schlaubi.dashboard.models.ActionValue
import dev.schlaubi.dashboard.models.ActionValueBinding
import dev.schlaubi.dashboard.models.ActionValueVariable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject

// https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/json.md#decoding-json-elements
typealias Decoder<T> = (JsonElement) -> T
class ActionContext : ModuleContext<List<ActionValue>>() {
    class RunContext(
        private val variableActions: Map<String, ActionData<*>>,
        private val bindingActions: Map<String, ActionData<*>>,
        private val data: JsonElement
    ) {
        fun <T : Any> fromVariable(variable: VariableData<T>): T {
            val action = (variableActions[variable.id]
                ?: throw IllegalArgumentException("Variable ${variable.name} not found in data. Did you register it with `useVariable`?")) as ActionData<T>


            val entry = data.jsonObject[variable.id]
                ?: throw IllegalArgumentException("Variable ${variable.id} not found in data. This is not supposed to happen. Please report this bug.")

            return action.decode(entry)
        }

        fun fromBinding(binding: BindingData): String {
            val action = (bindingActions[binding.id]
                ?: throw IllegalArgumentException("Variable ${binding.identifier} not found in data. Did you register it with `useVariable`?")) as ActionData<String>

            val entry = data.jsonObject[binding.id]
                ?: throw IllegalArgumentException("Variable ${binding.id} not found in data. This is not supposed to happen. Please report this bug.")

            return action.decode(entry)
        }
    }

    data class ActionData<T : Any>(
        val id: String, val varname: String, val decode: Decoder<T>
    )

    val variableActions = mutableListOf<ActionData<*>>()
    val bindingActions = mutableListOf<ActionData<*>>()
    val runners = mutableListOf<suspend RunContext.() -> Unit>()

    inline fun <reified T : Any> decode(json: JsonElement): T {
        return Json.decodeFromJsonElement(json)
    }

    inline fun <reified T : Any> useVariable(variable: VariableData<T>) {
        variableActions.add(ActionData<T>((variableActions.count() + 1).toString(),
            variable.name,
            decode = { decode(it) }))
    }

    fun useBinding(binding: BindingData) {
        bindingActions.add(ActionData<String>((bindingActions.count() + 1).toString(),
            binding.identifier,
            decode = { decode(it) }))
    }

    fun run(cb: suspend RunContext.() -> Unit) {
        runners.add(cb)
    }

    private suspend fun runWithData(data: JsonElement) {
        val runContext = RunContext(variableActions.associateBy { it.id },
            bindingActions.associateBy { it.id }, data)
        for (runner in runners) {
            runner(runContext)
        }
    }

    override fun toConfig(): PageModuleConfig<List<ActionValue>> {
        val route = "/action/${next()}"
        val actions = variableActions.map {
            ActionValueVariable(it.id, it.varname)
        } + bindingActions.map {
            ActionValueBinding(it.id, it.varname)
        }
        return PageModuleConfig(
            routes = listOf(ActionRoute(
                route
            ) { runWithData(it) }) + routes,
            variables = variables,
            config = actions
        )
    }

    companion object ActionCounter {
        private var counter = 0

        fun next(): Int {
            return counter++
        }
    }
}