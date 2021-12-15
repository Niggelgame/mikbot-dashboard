import type { PageElementConfig } from "../page_element_config";

export let HTML_INPUT_ELEMENT_TYPE = "rawhtml";

export interface HtmlInputElementConfig  extends PageElementConfig {
    raw_html: string;
}