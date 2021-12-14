import type { PageElementConfig } from "./page_element_config";
import type { VariableConfig } from "./variable_config";

export interface PageConfig {
    name: string;
    path?: string;
    elements: PageElementConfig[];
    variables?: VariableConfig[];
}