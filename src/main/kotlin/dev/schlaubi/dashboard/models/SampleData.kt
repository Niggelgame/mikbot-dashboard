package dev.schlaubi.dashboard.models

import dev.schlaubi.dashboard.models.pageelements.*

val sampleData = BaseConfig(
    modules = listOf(
        ModuleConfig(
            name = "module1",
            pages = listOf(
                PageConfig(
                    name = "page1",
                    variables = listOf(
                        DirectVariableConfig(
                            id = "title",
                            value = "Dies ist der Titel"
                        ),
                        RequestVariableConfig(
                            id = "var2",
                            placeholder = "Loading",
                            url = "/assets/sample.json"
                        )
                    ),
                    elements = listOf(
                        TextElementConfig(
                            value = VarPageVariable("title"),
                            options = TextElementOptions(
                                color = "red"
                            )
                        ),
                        TextElementConfig(
                            value = DirectPageVariable("HALLOOOO"),
                            options = TextElementOptions(
                                color = "red"
                            )
                        ),
                        RowElementConfig(
                            mainAxis = "around",
                            crossAxis = "around",
                            elements = listOf(
                                TextInputElementConfig(
                                    inputIdentifier = "element2",
                                    label = "Element 2",
                                    required = true,
                                    options = TextInputOptions(
                                        type = "password"
                                    ),
                                    inputValidation = listOf(
                                        TextInputValidation(
                                            type = TextInputValidationType.REGEX,
                                            value = "^[a-zA-Z0-9]*\$"
                                        )
                                    )
                                ),
                                TextInputElementConfig(
                                    inputIdentifier = "element3",
                                    label = "Element 3",
                                    required = true,
                                    options = TextInputOptions(
                                        type = "password"
                                    ),
                                    inputValidation = listOf(
                                        TextInputValidation(
                                            type = TextInputValidationType.REGEX,
                                            value = "^[a-zA-Z0-9]*\$"
                                        ),
                                        TextInputValidation(
                                            type = TextInputValidationType.EQ_INPUT,
                                            value = "element3"
                                        )
                                    )
                                ),
                                SpacerElementConfig(),
                                ButtonInputElementConfig(
                                    label = "Element 4",
                                    options = ButtonInputOption(
                                        color = "yellow",
                                    ),
                                    action = ButtonSubmitAction(
                                        values = listOf(
                                            ButtonSubmitActionValueBinding(
                                                key = "password_one",
                                                identifier = "element2"
                                            ),
                                            ButtonSubmitActionValueBinding(
                                                key = "password_two",
                                                identifier = "element3"
                                            ),
                                            ButtonSubmitActionValueVariable(
                                                key = "password_three",
                                                variable = "element4"
                                            )
                                        ),
                                        endpoint = "/submitpassword00001"
                                    )
                                )
                            )
                        ),
                        ColumnElementConfig(
                            mainAxis = "start",
                            crossAxis = "center",
                            elements = listOf(
                                ImageElementConfig(
                                    url = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png",
                                    alt = "Google Logo",
                                    options = ImageElementOptions(
                                        width = "100px",
                                        height = "100px"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    )
)