import type { PageElementConfig } from "../page_element_config";
import type { Variable } from "../value_configs";

export interface TextElementConfig extends PageElementConfig {
    value: Variable;
    options?: TextElementOptions;
}

export interface TextElementOptions {
    color?: string;
}