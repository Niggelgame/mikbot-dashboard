import type { PageElementConfig } from "../page_element_config";

export interface ButtonInputElementConfig extends PageElementConfig {
    label: string;
    options: ButtonInputOption[];
    action: ButtonAction;
}

export interface ButtonInputOption {
    color: string;
}

export interface ButtonAction {
    type: string;
}

export interface ButtonSubmitAction extends ButtonAction {
    endpoint: string;
    method?: string;
    values: {
        key: string;
        identifier: string;
    }[];
}