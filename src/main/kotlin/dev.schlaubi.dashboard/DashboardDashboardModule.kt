package dev.schlaubi.dashboard

import dev.schlaubi.dashboard.api.DashboardExtensionPoint
import dev.schlaubi.dashboard.api.DashboardModuleContext
import dev.schlaubi.dashboard.api.MinimalLengthTextInputValidator
import dev.schlaubi.dashboard.api.getProperty
import org.pf4j.Extension

@Extension
class TestModule : DashboardExtensionPoint {
    override val moduleName: String = "TestModule"

    override fun DashboardModuleContext.register() {


        page("Test") {
            val variable = newVariable("test")
            val var2 = newFutureVariable {
                "test2"
            }

            val var3 = newFutureVariable {
                Test("hi", Test2(Test3("test3")), "test4")
            }

            val varValue = var3.getProperty(Test::b).getProperty(Test2::f).getProperty(Test3::g)

            val inputController1 = newTextInputController(varValue)

            println("varValue: ${varValue.path.joinToString(".")}")

            button("Test") {
                action {
                    useVariable(variable)
                    useVariable(var2)
                    useVariable(var3)
                    useVariable(varValue)

                    useTextInputController(inputController1)

                    run {
                        val value = fromVariable(variable)
                        val value2 = fromVariable(var2)
                        val value3 = fromTextInputController(inputController1)
                        val test = fromVariable(var3)
                        val superTest = fromVariable(varValue)

                        updateVariable(var2, "neuer wert")
                        println("Value: $value, $value2, $value3, $test, $superTest")
                    }
                }
            }

            text(var2)

            text("Hi")

            text(var3.getProperty(Test::a))

            val inputController2 = newTextInputController(newVariable(""), listOf(MinimalLengthTextInputValidator(3)))

            textInput(inputController2) {
                label = "Test2"
            }

            button("Test2") {
                action {
                    useTextInputController(inputController2)

                    run {
                        val value = fromTextInputController(inputController2)
                        println("Value: $value")
                    }
                }
            }

            val goodVar = newVariable("Hiii")

            text("Binding Tests")
            text("Sub Variables")
            textInput(inputController1)
            text(varValue)
            text("Base Variables")
            textInput(newTextInputController(goodVar))
            text(goodVar)
        }
    }
}

