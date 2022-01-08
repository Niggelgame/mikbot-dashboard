import type { PageConfig } from "src/lib/models/page_config";
import type { DirectVariableConfig, RequestVariableConfig } from "../../models/variable_config";
import type { Writable } from "svelte/store";

import { VARIABLE_TYPE_DIRECT, VARIABLE_TYPE_REQUEST } from "../../models/variable_config";
import { writable } from "svelte/store";
import Api from "../api/api";
import type { TextInputController, TextInputValidation } from "src/lib/models/page_elements/text_input_element_config";


export interface ExtendedWritable<T> extends Writable<T> {
    setIfNull(value: T): void;
    dispose(): void;
    onError(fn: (error: any) => void): () => void;
}

export abstract class Variable {
    private errorHandler: ((error: any) => void)[] = [];

    handleError(e: any) {
        this.errorHandler.forEach(fn => fn(e));
    }

    addErrorHander(fn: (error: any) => void) : () => void {
        this.errorHandler.push(fn);
        return () => {
            this.errorHandler = this.errorHandler.filter(f => f !== fn);
        }
    }

    abstract getValue(): Promise<any>;
    abstract getWriteableValue(): ExtendedWritable<any>;
}

class _AsyncVariable extends Variable {
    private value?: any;
    private loaded: boolean = false;
    private load: () => Promise<any>;
    private _writable: ExtendedWritable<any>;

    constructor(load: () => Promise<any>) {
        super();
        this.load = load;
        const { subscribe, set, update } = writable(null);

        this._writable = {
            subscribe,
            set: (value) => {
                this.value = value;
                set(value);
            },
            setIfNull: (value) => {
                if (this.value === undefined) {
                    this.value = value;
                    set(value);
                }
            },
            update: (fn) => {
                this.value = fn(this.value);
                update(fn);
            },
            dispose: () => { },
            onError: (fn) => { return this.addErrorHander(fn)  },
        };
    }

    public async getValue(): Promise<any> {
        if (this.loaded && this.value !== undefined) {
            return this.value;
        }

        try {
            this.value = await this.load();
        } catch (e: any) {
            this.handleError(e);
        }
        
        this.loaded = true;

        this._writable.set(this.value);
        return this.value;
    }

    getWriteableValue(): ExtendedWritable<any> {
        return this._writable;
    }
}

class _DirectVariable<T> extends Variable {
    private value?: T;
    private _writable: ExtendedWritable<T>;

    constructor(value: T) {
        super();
        this.value = value;
        const { subscribe, set, update } = writable(value);
        this._writable = {
            subscribe,
            set: (value) => {
                this.value = value;
                set(value);
            },
            setIfNull: (value) => {
                if (this.value === undefined) {
                    this.value = value;
                    set(value);
                }
            },
            update: (fn) => {
                this.value = fn(this.value);
                update(fn);
            },
            dispose: () => { },
            onError: (fn) => {  return this.addErrorHander(fn) },
        };
    }

    async getValue(): Promise<T> {
        return this.value;
    }

    getWriteableValue(): ExtendedWritable<T> {
        return this._writable;
    }
}

// Page should be of format "module/page"

export class BasicVariableService {
    private variables: { [key: string]: { [key: string]: Variable } } = {};

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
                    this.variables[pagename][v.id] = new _DirectVariable(v.value);
                } else if (variable.type === VARIABLE_TYPE_REQUEST) {
                    let v: RequestVariableConfig = variable as RequestVariableConfig;
                    let va = new _AsyncVariable(async () => {
                        // Receive value from action Route result
                        return JSON.parse((await Api.instance.variableFromRequestVariableConfig(v)).data);
                    });
                    if (v.lazy) {
                        va.getValue();
                    }
                    this.variables[pagename][v.id] = va;
                }
            }
        }

        console.info("Finished loading variables for " + pagename);
    }

    // With page of format "module/page"
    public getVariable(page: string, id: string): Variable {
        if (this.variables[page]) {
            return this.variables[page][id];
            // if (typeof v === "string") {
            //     return v;
            // } else if (v instanceof _Variable) {
            //     return v.getValue();
            // }
        } else {
            console.warn(`Variable ${id} not found for page ${page}`);
        }
    }
}

export class InputControllerService {
    private controllers: { [key: string]: { [key: string]: TextInputController } } = {};

    registerController(page: string, controller: TextInputController) {
        if (!this.controllers[page]) {
            this.controllers[page] = {};
        }
        this.controllers[page][controller.input_identifier] = controller;
    }

    getController(page: string, input_identifier: string): TextInputController {
        if (!this.controllers[page]) {
            return null;
        }
        if (!this.controllers[page][input_identifier]) {
            return null;
        }
        return this.controllers[page][input_identifier];
    }
}

// export interface WritableData {
//     value: Writable<string>;
//     validators: TextInputValidation[];
// }

// // Returns Bindings for 
// export class BindingVariableService {
//     private variables: { [key: string]: { [key: string]: WritableData } } = {};

//     public getVariableForPage(page: string, id: string, defaultValue?: string, validators?: TextInputValidation[]): WritableData {
//         if (!this.variables[page]) {
//             this.variables[page] = {};
//         }
//         if (!this.variables[page][id]) {
//             this.variables[page][id] = {value: writable(defaultValue), validators: validators};
//         }
//         return this.variables[page][id];
//     }

//     public clearVariableForPage(page: string, id: string) {
//         if (this.variables[page]) {
//             this.variables[page][id].value.set(undefined);
//         }
//     }
// }