import type { PageConfig } from "src/lib/models/page_config";
import type { Writable } from "svelte/store";
import { BasicVariableService, BindingVariableService } from "../variable_service/variable_service"

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

    public loadVariablesForPage(page: PageConfig, module:string) {
        this.basicVariableService.loadVariablesForPage(page, module);
    }

    public getBasicVariable(page: string, id: string): string | Promise<any> | void {
        return this.basicVariableService.getVariable(page, id);
    }

    public getVariableBinding(page: string, id: string, defaultValue?: string): Writable<string> {
        return this.bindingVariableService.getVariableForPage(page, id, defaultValue);
    }
}