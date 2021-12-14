

import type { PageElementConfig } from "../page_element_config";


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
