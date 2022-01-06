import type { PageConfig } from "src/lib/models/page_config";
import type { Writable } from "svelte/store";
import { validateValue } from "../utils/validator";
import { BasicVariableService, InputControllerService, Variable } from "../variable_service/variable_service"
import type { TextInputController, TextInputValidation } from "src/lib/models/page_elements/text_input_element_config";

export class VariableRepository {
    private static _instance: VariableRepository = new VariableRepository();

    constructor() {
        if (VariableRepository._instance)
            throw new Error("Use Singleton.instance");
        VariableRepository._instance = this;
    }

    static get instance() {
        return VariableRepository._instance;
    }

    private inputControllerService = new InputControllerService();
    private basicVariableService = new BasicVariableService();

    public loadVariablesForPage(page: PageConfig, module: string) {
        this.basicVariableService.loadVariablesForPage(page, module);
    }

    public getBasicVariable(page: string, id: string): Variable {
        return this.basicVariableService.getVariable(page, id);
    }

    // public getVariableBinding(page: string, id: string, defaultValue?: string, validators?: TextInputValidation[]): WritableData {
    //     return this.bindingVariableService.getVariableForPage(page, id, defaultValue, validators);
    // }

    public registerController(page: string, controller: TextInputController) {
        this.inputControllerService.registerController(page, controller);
    }

    private get__store<T>(store: Writable<T>): T {
        let $val
        store.subscribe($ => $val = $)()
        return $val
    }

    public async getVariableBindingValueValidated(page: string, controllerId: string): Promise<string> {
        let controller = this.inputControllerService.getController(page, controllerId);
        let bindingResult = this.getBasicVariable(page, controller.binding_variable.value);

        let storeValue = await bindingResult.getValue();

        let validationResult = await validateValue(storeValue, page, controller.input_validation, controller.required);

        if (validationResult) {
            throw new Error(validationResult);
        }

        return storeValue;
    }

    public async getVariableBindingValueRaw(page: string, id: string): Promise<string> {
        let controller = this.inputControllerService.getController(page, id);
        let storeValue = await this.getBasicVariable(page, controller.binding_variable.value).getValue();
        return storeValue
    }
}