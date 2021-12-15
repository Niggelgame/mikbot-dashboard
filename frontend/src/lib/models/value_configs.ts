export type Variable = string | ValueConfig;

export interface ValueConfig {
    type: string;
}

export let VALUE_TYPE_VAR = "var";

export interface VarConfigValue extends ValueConfig {
    value: string;
    placeholder?: string;
    property?: string[];
}

export let VALUE_TYPE_DIRECT = "direct";

export interface DirectConfigValue extends ValueConfig {
    value: string;
}