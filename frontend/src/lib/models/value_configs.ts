export interface Variable {
    type: string;
}

export let VALUE_TYPE_VAR = "var";

export interface VarConfigValue extends Variable {
    value: string;
    placeholder?: string;
    property?: string[];
}

export let VALUE_TYPE_DIRECT = "direct";

export interface DirectConfigValue extends Variable {
    value: string;
}