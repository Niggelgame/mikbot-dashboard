import type { PageElementConfig } from "../page_element_config";

export let SIZED_BOX_ELEMENT_TYPE = "sizedbox";

export interface SizedBoxElementConfig extends PageElementConfig {
    width?: number;
    height?: number;
}