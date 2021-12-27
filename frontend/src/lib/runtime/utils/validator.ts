import { TEXT_INPUT_VALIDATION_TYPE_EQUAL_TO_INPUT, TEXT_INPUT_VALIDATION_TYPE_MIN_LENGTH, TEXT_INPUT_VALIDATION_TYPE_REGEX } from "../../models/page_elements/text_input_element_config";
import type { TextInputValidation } from "src/lib/models/page_elements/text_input_element_config";
import { VariableRepository } from "../respositories/variable_repository";

export function validateValue(value: string, page: string, validators?: TextInputValidation[]): string | void {
    if (validators?.length > 0) {
        for (let validator of validators) {
            if (validator.type === TEXT_INPUT_VALIDATION_TYPE_EQUAL_TO_INPUT) {
                let otherValue = VariableRepository.instance.getVariableBindingValueRaw(page, validator.value);
                if (value !== otherValue) {
                    return `Value must equal ${validator.value}`;
                }
            } else if (validator.type === TEXT_INPUT_VALIDATION_TYPE_MIN_LENGTH) {
                if (value.length < parseInt(validator.value)) {
                    return `Value must be at least ${validator.value} characters long`;
                }
            } else if (validator.type === TEXT_INPUT_VALIDATION_TYPE_REGEX) {
                if(value === null) {
                    return `Value must match ${validator.value}`;
                }
                if (!value.match(validator.value)) {
                    return `Value must match ${validator.value}`;
                }
            }
        }
    } else {
        return void 0;
    }
}