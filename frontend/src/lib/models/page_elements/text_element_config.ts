import type { PageElementConfig } from "../page_element_config";
import type { Variable } from "../value_configs";

export let TEXT_ELEMENT_TYPE = "text";

export interface TextElementConfig extends PageElementConfig {
    value: Variable;
    options?: TextElementOptions;
}

export interface TextElementOptions {
    color?: string;
    font_size?: string;
}