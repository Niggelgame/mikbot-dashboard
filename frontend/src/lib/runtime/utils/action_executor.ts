import type { BindingButtonSubmitActionValue, ButtonAction, ButtonSubmitAction, VariableButtonSubmitActionValue } from "../../models/page_elements/button_input_element_config";
import {BUTTON_ACTION_TYPE_SUBMIT, BUTTON_ACTION_VALUE_BINDING, BUTTON_ACTION_VALUE_VARIABLE} from "../../models/page_elements/button_input_element_config";
import Api from "../api/api";
import { VariableRepository } from "../respositories/variable_repository";

export let runAction = async (action: ButtonAction, page: string) : Promise<void> => {
    function get__store(store) {
        let $val
        store.subscribe($ => $val = $)()
        return $val
      }

    if(action.type === BUTTON_ACTION_TYPE_SUBMIT) {
        let submitAction = action as ButtonSubmitAction;
        let method = submitAction.method ?? (!!(submitAction.values?.length > 0)) ? "POST" : "GET";
        
        let data = {};
        for(let value of submitAction.values ?? []) {
            if(value.type === BUTTON_ACTION_VALUE_VARIABLE) {
                let variableValue = value as VariableButtonSubmitActionValue;
                let variable = await VariableRepository.instance.getBasicVariable(page, variableValue.var);
                data[value.key] = variable;
            } else if(value.type === BUTTON_ACTION_VALUE_BINDING) {
                let bindingValue = value as BindingButtonSubmitActionValue;
                let binding = await VariableRepository.instance.getVariableBinding(page, bindingValue.identifier);
                data[value.key] = get__store(binding);
            }
        }

        let response = await Api.instance.runAction(submitAction.endpoint, method, data);
        let responseText = await response.text();
        console.log(responseText);
    } else {
        throw new Error("Unknown action type: " + action.type);
    }
}