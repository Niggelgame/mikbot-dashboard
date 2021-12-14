import type { PageElementConfig } from "../page_element_config";


export interface ColumnElementConfig extends PageElementConfig {
    main_axis?: string;
    cross_axis?: string;
    elements: PageElementConfig[];
}
