export interface Variable {
    type: string;
    value: string;
}

export let VALUE_TYPE_VAR = "var";

export interface VarConfigValue extends Variable {
    placeholder?: string;
    property?: string[];
}