import type { ActionResponse } from "../../models/action_response";
import type { ButtonAction, ButtonSubmitAction, ControllerButtonSubmitActionValue, VariableButtonSubmitActionValue} from "../../models/page_elements/button_input_element_config";
import { BUTTON_ACTION_VALUE_CONTROLLER } from "../../models/page_elements/button_input_element_config";
import {BUTTON_ACTION_TYPE_SUBMIT, BUTTON_ACTION_VALUE_VARIABLE} from "../../models/page_elements/button_input_element_config";
import Api from "../api/api";
import { VariableRepository } from "../respositories/variable_repository";
import { PropertyWritable } from "./value_receiver";

export let runAction = async (action: ButtonAction, page: string) : Promise<void> => {
    if(action.type === BUTTON_ACTION_TYPE_SUBMIT) {
        let submitAction = action as ButtonSubmitAction;
        let method = submitAction.method ?? (!!(submitAction.values?.length > 0)) ? "POST" : "GET";
        
        let data = {};
        for(let value of submitAction.values ?? []) {
            if(value.type === BUTTON_ACTION_VALUE_VARIABLE) {
                let variableValue = value as VariableButtonSubmitActionValue;
                let variable = await VariableRepository.instance.getBasicVariable(page, variableValue.variable);
                data[value.key] = await variable.getValue();
            } else if(value.type === BUTTON_ACTION_VALUE_CONTROLLER) {
                let bindingValue = value as ControllerButtonSubmitActionValue;
                let binding = await VariableRepository.instance.getVariableBindingValueValidated(page, bindingValue.variable);
                data[value.key] = binding;
            }
        }

        let response = await Api.instance.runAction(submitAction.endpoint, method, data);
        let responseData = (await response.json()) as ActionResponse;
        let variablesToUpdate = responseData.variable_updates;

        if(variablesToUpdate) {
            let updatetableVariables = Object.entries(variablesToUpdate) as [string, any][];
            for (const [variableId, variableToUpdate] of updatetableVariables) {
                let updateVariable = VariableRepository.instance.getBasicVariable(page, variableId);
                let variableToUpdateWritable = updateVariable.getWriteableValue();
                if(variableToUpdate.path && variableToUpdate.path.length > 0) {
                    let propertyWritable = new PropertyWritable(variableToUpdateWritable, variableToUpdate.path)
                    propertyWritable.set(variableToUpdate.value);
                    propertyWritable.dispose();
                } else {
                    variableToUpdateWritable.set(variableToUpdate.value)
                }
            }
        }
        

        console.log(responseData.data);
    } else {
        throw new Error("Unknown action type: " + action.type);
    }
}