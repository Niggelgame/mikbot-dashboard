import type { PageElementConfig } from "../page_element_config";

export interface HtmlInputElementConfig  extends PageElementConfig {
    raw_html: string;
}