import type { DirectConfigValue, ValueConfig, VarConfigValue, Variable } from "src/lib/models/value_configs";
import { VALUE_TYPE_DIRECT, VALUE_TYPE_VAR } from "../../models/value_configs";
import { readable } from "svelte/store";
import { VariableRepository } from "../respositories/variable_repository";

export let getValue = (variable: Variable, page: string) => {
    return readable(null, function start(set) {
        if(typeof variable === "string") {
            set(variable);
        }
        const config = variable as ValueConfig;
        if (config.type === VALUE_TYPE_DIRECT) {
            const directConfig = config as DirectConfigValue;
            set(directConfig.value);
        } else if (config.type === VALUE_TYPE_VAR) {
            const varConfig = config as VarConfigValue;
            set(varConfig.placeholder);
            const result = VariableRepository.instance.getBasicVariable(page, varConfig.value);
            console.log("result", result);
            if (result) {
                if(typeof result === "string") {
                    set(result);
                } else {
                    result.then((value) => {
                        // Get property of result object
                        if(varConfig.property && varConfig.property.length > 0) {
                            let result = value;
                            varConfig.property.forEach((property) => {
                                result = result[property];
                            });
                            set(result);
                        } else {
                            set(value);
                        }
                    });
                }
            }
        }
    });
}