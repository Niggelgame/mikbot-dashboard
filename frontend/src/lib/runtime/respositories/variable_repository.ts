import type { PageConfig } from "src/lib/models/page_config";
import type { Writable } from "svelte/store";
import { validateValue } from "../utils/validator";
import { BasicVariableService, BindingVariableService } from "../variable_service/variable_service"
import type { WritableData } from "../variable_service/variable_service";
import type { TextInputValidation } from "src/lib/models/page_elements/text_input_element_config";

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

    private bindingVariableService = new BindingVariableService();
    private basicVariableService = new BasicVariableService();

    public loadVariablesForPage(page: PageConfig, module: string) {
        this.basicVariableService.loadVariablesForPage(page, module);
    }

    public getBasicVariable(page: string, id: string): string | Promise<any> | void {
        return this.basicVariableService.getVariable(page, id);
    }

    public getVariableBinding(page: string, id: string, defaultValue?: string, validators?: TextInputValidation[]): WritableData {
        return this.bindingVariableService.getVariableForPage(page, id, defaultValue, validators);
    }

    private get__store<T>(store: Writable<T>): T {
        let $val
        store.subscribe($ => $val = $)()
        return $val
    }

    public getVariableBindingValueValidated(page: string, id: string): string {
        let store = this.getVariableBinding(page, id);

        let storeValue = this.get__store(store.value);

        let validationResult = validateValue(storeValue, page, store.validators);

        if (validationResult) {
            throw new Error(validationResult);
        }

        return storeValue;
    }

    public getVariableBindingValueRaw(page: string, id: string): string {
        let store = this.getVariableBinding(page, id);
        return this.get__store(store.value);
    }
}