export interface VariableConfig {
    id: string;
    type: string;
}

export let VARIABLE_TYPE_DIRECT = "direct";

export interface DirectVariableConfig extends VariableConfig {
    value: string;
}

export let VARIABLE_TYPE_REQUEST = "request";

export interface RequestVariableConfig extends VariableConfig {
    lazy?: boolean;
    url: string;
    method?: string;
    body?: string;
    headers?: { [key: string]: string };
    placeholder?: string;
}