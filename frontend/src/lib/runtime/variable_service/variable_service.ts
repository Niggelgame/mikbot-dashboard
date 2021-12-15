import type { PageConfig } from "src/lib/models/page_config";
import type { DirectVariableConfig, RequestVariableConfig } from "../../models/variable_config";
import type { Writable } from "svelte/store";

import { VARIABLE_TYPE_DIRECT, VARIABLE_TYPE_REQUEST } from "../../models/variable_config";
import { writable } from "svelte/store";
import Api from "../api/api";

class _Variable {
    private value?: any;
    private loaded: boolean = false;
    private load: () => Promise<any>;

    constructor(load: () => Promise<any>) {
        this.load = load;
    }

    public async getValue(): Promise<any> {
        if (this.loaded && this.value !== undefined) {
            return this.value;
        }

        this.value = await this.load();
        this.loaded = true;

        return this.value;
    }
}

// Page should be of format "module/page"

export class BasicVariableService {
    private variables: { [key: string]: { [key: string]: string | _Variable } } = {};

    public loadVariablesForPage(page: PageConfig, module: string) {
        const pagename = `${module}/${page.name}`;

        console.info("Loading variables for " + pagename);

        // Clear out variable store of previous page visit 
        // TODO: Maybe we should keep the variables for the previous page?
        this.variables[pagename] = {};

        if (page.variables) {
            for (let variable of page.variables) {
                if (variable.type === VARIABLE_TYPE_DIRECT) {
                    let v: DirectVariableConfig = variable as DirectVariableConfig;
                    this.variables[pagename][v.id] = v.value;
                } else if (variable.type === VARIABLE_TYPE_REQUEST) {
                    let v: RequestVariableConfig = variable as RequestVariableConfig;
                    let va = new _Variable(async () => {
                        return await Api.instance.variableFromRequestVariableConfig(v);
                    });
                    if (v.lazy) {
                        va.getValue();
                    }
                    this.variables[pagename][v.id] = va;
                }
            }
        }
    }

    // With page of format "module/page"
    public getVariable(page: string, id: string): string | Promise<any> | void {
        if (this.variables[page]) {
            let v = this.variables[page][id];
            if (typeof v === "string") {
                return v;
            } else if (v instanceof _Variable) {
                return v.getValue();
            }
        } else {
            console.warn(`Variables found for page ${page}`);
        }
    }
}

// Returns Bindings for 
export class BindingVariableService {
    private variables: { [key: string]: { [key: string]: Writable<string> } } = {};

    public getVariableForPage(page: string, id: string, defaultValue?: string): Writable<string> {
        if (!this.variables[page]) {
            this.variables[page] = {};
        }
        if (!this.variables[page][id]) {
            this.variables[page][id] = writable(defaultValue);
        }
        return this.variables[page][id];
    }

    public clearVariableForPage(page: string, id: string) {
        if (this.variables[page]) {
            this.variables[page][id].set(undefined);
        }
    }
}