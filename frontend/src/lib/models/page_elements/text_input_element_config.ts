import type { PageElementConfig } from "../page_element_config";
import type { Variable } from "../value_configs";

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
    type: string;
}

export interface TextInputValidation {
    type: string;
    value: string;
}