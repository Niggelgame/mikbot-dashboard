import type { VarConfigValue, Variable } from "src/lib/models/value_configs";
import { VALUE_TYPE_VAR } from "../../models/value_configs";
import type { Subscriber, Unsubscriber, Updater } from "svelte/store";
import { VariableRepository } from "../respositories/variable_repository";
import type { ExtendedWritable } from "../variable_service/variable_service";

export class PropertyWritable<T, R> implements ExtendedWritable<T> {
    private _value: R;
    private _baseWritable: ExtendedWritable<R>;
    private _property: string[];

    private _unsubscribe: Unsubscriber;

    constructor(baseWritable: ExtendedWritable<R>, property: string[]) {
        this._baseWritable = baseWritable;
        this._property = property;

        // Find better solution
        this._baseWritable.subscribe((v) => {
            this._value = v;
        })
    }

    onError(fn: (error: any) => void): () => void {
        return this._baseWritable.onError(fn);
    }

    dispose(): void {
        this._unsubscribe();
    }

    setIfNull(value: T): void {
        this._baseWritable.setIfNull(this.setProperty(this._value, value));
    }
    set(value: T): void {
        this._baseWritable.setIfNull(this.setProperty(this._value, value));
    }
    update(updater: Updater<T>): void {
        this._baseWritable.update((v) => {
            return this.setProperty(v, updater(this.getProperty(v)));
        });
    }
    subscribe(run: Subscriber<T>, invalidate?: (value?: T) => void): Unsubscriber {
        return this._baseWritable.subscribe((v) => {
            run(this.getProperty(v));
        }, (v) => {
            if (invalidate !== undefined) {
                invalidate(this.getProperty(v));
            }
        })
    }

    private getProperty(value: R): T {
        if(value === undefined || value === null) {
            return undefined;
        }
        let currentValue = value;
        for (let i = 0; i < this._property.length; i++) {
            currentValue = currentValue[this._property[i]];
        }
        return (currentValue as unknown as T);
    }

    private setProperty(baseValue: R, newValue: T): R {
        let currentValue = baseValue;
        for (let i = 0; i < this._property.length - 1; i++) {
            if (!currentValue[this._property[i]]) {
                currentValue[this._property[i]] = {};
            }
            currentValue = currentValue[this._property[i]];
        }
        currentValue[this._property[this._property.length - 1]] = newValue;
        return baseValue;
    }

}

export let getValue = (variable: Variable, page: string) => {

    if (variable.type === VALUE_TYPE_VAR) {
        const varConfig = variable as VarConfigValue;

        const result = VariableRepository.instance.getBasicVariable(page, varConfig.value);

        let _writable = result.getWriteableValue();

        if (varConfig.property && varConfig.property.length > 0) {
            return new PropertyWritable(_writable, varConfig.property);
        } else {
            return _writable;
        }
        // // console.log("result", result);
        // if (result) {
        //     if (typeof result === "string") {
        //         set(result);
        //     } else {
        //         result.then((value) => {
        //             // Get property of result object
        //             if (varConfig.property && varConfig.property.length > 0) {
        //                 let result = value;
        //                 varConfig.property.forEach((property) => {
        //                     result = result[property];
        //                 });
        //                 set(result);
        //             } else {
        //                 set(value);
        //             }
        //         });
        //     }
        // }
    }
}