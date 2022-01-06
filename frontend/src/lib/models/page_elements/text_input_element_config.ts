import type { PageElementConfig } from "../page_element_config";
import type { Variable } from "../value_configs";

export let TEXT_INPUT_ELEMENT_TYPE = "textinput";

export interface TextInputElementConfig  extends PageElementConfig {
    controller: TextInputController;
    label?: string;
    placeholder?: Variable;
    options?: TextInputOptions;
}

export interface TextInputController {
    input_identifier: string;
    binding_variable: Variable;
    input_validation?: TextInputValidation[];
    required?: boolean;
}

export interface TextInputOptions {
    type?: string;
}

export let TEXT_INPUT_VALIDATION_TYPE_REGEX = "regex";
export let TEXT_INPUT_VALIDATION_TYPE_MIN_LENGTH = "min_length";
export let TEXT_INPUT_VALIDATION_TYPE_EQUAL_TO_INPUT = "eq_input";

export interface TextInputValidation {
    type: string;
    value: string;
}