import type { PageElementConfig } from "../page_element_config";
import type { Variable } from "../value_configs";

export let TEXT_INPUT_ELEMENT_TYPE = "textinput";

export interface TextInputElementConfig  extends PageElementConfig {
    input_identifier: string;
    label?: string;
    default?: Variable;
    placeholder?: Variable;
    required?: boolean;
    options?: TextInputOptions;
    input_validation?: TextInputValidation[];
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