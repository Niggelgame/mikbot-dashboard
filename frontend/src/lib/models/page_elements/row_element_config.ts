import type { PageElementConfig } from "../page_element_config";


export interface RowElementConfig extends PageElementConfig {
    main_axis?: string;
    cross_axis?: string;
    elements: PageElementConfig[];
}
