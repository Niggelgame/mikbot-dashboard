import type { PageElementConfig } from "../page_element_config";

export let SPACER_ELEMENT_TYPE = "spacer";

export interface SpacerElementConfig extends PageElementConfig {
    flex?: number;
    child?: PageElementConfig;
}

