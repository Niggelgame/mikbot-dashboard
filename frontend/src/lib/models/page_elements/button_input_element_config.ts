import type { PageElementConfig } from "../page_element_config";

export let BUTTON_ELEMENT_TYPE = "button";

export interface ButtonInputElementConfig extends PageElementConfig {
    label: string;
    options?: ButtonInputOption;
    action: ButtonAction;
}

export interface ButtonInputOption {
    color?: string;
    width?: string;
    height?: string;
}

export interface ButtonAction {
    type: string;
}

export let BUTTON_ACTION_TYPE_SUBMIT = "submit";

export interface ButtonSubmitAction extends ButtonAction {
    endpoint: string;
    method?: string;
    values?: ButtonSubmitActionValue[];
}

export interface ButtonSubmitActionValue {
    key: string;
    type: string;
}

export let BUTTON_ACTION_VALUE_VARIABLE = "var";

export interface VariableButtonSubmitActionValue extends ButtonSubmitActionValue {
    variable: string;
}

export let BUTTON_ACTION_VALUE_BINDING = "binding";

export interface BindingButtonSubmitActionValue extends ButtonSubmitActionValue {
    identifier: string;
}