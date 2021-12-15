import type { PageElementConfig } from "../page_element_config";

export let ROW_ELEMENT_TYPE = "row";

export interface RowElementConfig extends PageElementConfig {
    main_axis?: string;
    cross_axis?: string;
    elements: PageElementConfig[];
}
