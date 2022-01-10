package dev.schlaubi.dashboard.api

import dev.schlaubi.dashboard.models.pageelements.*
import java.util.*

enum class TextInputType {
    checkbox,
    color,
    date,
    datetime {
        override fun toString(): String = "datetime-local"
    },
    email,
    file,
    hidden,
    image,
    month,
    number,
    password,
    reset,
    tel,
    text,
    time,
    url,
    week
}

fun ModuleContext<*>.textInput(controllerContext: TextInputControllerContext<*>, cb: (TextInputContext.() -> Unit)? = null) {
    val textInput = TextInputContext(controllerContext)
    if(cb != null) textInput.apply(cb)
    modules.add(textInput)
}

class TextInputContext(private val controller: TextInputControllerContext<*>) :
    PageElementModuleContext<TextInputElementConfig>() {
    var placeholder: ValueProvider<*, String>? = null
    var label: String? = null
    var inputType: TextInputType = TextInputType.text

    override fun toConfig(): PageModuleConfig<TextInputElementConfig> {
        return PageModuleConfig(
            TextInputElementConfig(
                controller = controller.toTextInputController(),
                label = label,
                placeholder = placeholder?.toPageVariable(),
                options = TextInputOptions(
                    type = inputType.toString()
                )
            ),
            variables = variables,
            routes = routes
        )
    }
}

class TextInputControllerContext<T : Any>(
    val id: String = UUID.randomUUID().toString(),
    val parentVariable: ValueProvider<T, String>,
    val validators: List<TextInputValidator<*>>,
    val required: Boolean = true,
) {
    fun toTextInputController(): TextInputController {
        return TextInputController(
            id,
            parentVariable.toPageVariable(),
            validators.map { it.toTextInputValidation() },
            required
        )
    }
}

abstract class TextInputValidator<R : Any> {
    abstract fun validate(value: String, getVariableValue: (ValueProvider<R, String>) -> String): Boolean

    abstract fun getDependingVariables(): List<VariableData<*>>

    abstract fun toTextInputValidation(): TextInputValidation
}

class RegexTextInputValidator(
    private val regex: String
) : TextInputValidator<Any>() {
    override fun validate(value: String, getVariableValue: (ValueProvider<Any, String>) -> String): Boolean {
        return value.matches(Regex(regex))
    }

    override fun getDependingVariables(): List<VariableData<*>> {
        return emptyList()
    }

    override fun toTextInputValidation(): TextInputValidation {
        return TextInputValidation(
            type = TextInputValidationType.REGEX, regex
        )
    }
}

class MinimalLengthTextInputValidator(
    private val minLength: Int
) : TextInputValidator<Any>() {
    override fun validate(value: String, getVariableValue: (ValueProvider<Any, String>) -> String): Boolean {
        return value.length >= minLength
    }

    override fun getDependingVariables(): List<VariableData<*>> {
        return emptyList()
    }

    override fun toTextInputValidation(): TextInputValidation {
        return TextInputValidation(
            type = TextInputValidationType.MIN_LENGTH, minLength.toString()
        )
    }
}

class EqualsOtherBindingTextInputValidator<R : Any>(private val otherBinding: TextInputControllerContext<R>) :
    TextInputValidator<R>() {
    override fun validate(value: String, getVariableValue: (ValueProvider<R, String>) -> String): Boolean {
        return value == getVariableValue(otherBinding.parentVariable)
    }

    override fun getDependingVariables(): List<VariableData<*>> {
        return listOf(otherBinding.parentVariable.getBaseVariable())
    }

    override fun toTextInputValidation(): TextInputValidation {
        return TextInputValidation(
            type = TextInputValidationType.EQ_INPUT, otherBinding.id,
        )
    }
}