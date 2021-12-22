import type { PageElementConfig } from "./page_element_config";
import type { VariableConfig } from "./variable_config";

export interface PageConfig {
    name: string;
    elements: PageElementConfig[];
    variables?: VariableConfig[];
}