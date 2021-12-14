export type Variable = string | ValueConfigs;

export interface ValueConfigs {
    type: string;
}

export let VALUE_TYPE_VAR = "var";

export interface VarConfigValue extends ValueConfigs {
    value: string;
    placeholder?: string;
    property?: string[];
}

export let VALUE_TYPE_DIRECT = "var";

export interface DirectConfigValue extends ValueConfigs {
    value: string;
}