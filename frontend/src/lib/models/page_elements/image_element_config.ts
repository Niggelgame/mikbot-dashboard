

import type { PageElementConfig } from "../page_element_config";

export let IMAGE_ELEMENT_TYPE = "image";

export interface ImageElementConfig extends PageElementConfig {
    url: string;
    alt?: string;
    options?: ImageElementOptions;
}

export interface ImageElementOptions {
    caption?: string;
    width?: number;
    height?: number;
}
